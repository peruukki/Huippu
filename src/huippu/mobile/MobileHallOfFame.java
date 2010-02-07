package huippu.mobile;

import huippu.common.HallOfFame;
import huippu.common.Resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Screen;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

final class MobileHallOfFame extends HallOfFame
    implements CommandListener
{
    private final Command mCmdOK =
        new Command( Resources.TEXT_RETURN, Command.BACK, 1 );
    private final Command mCmdReset =
        new Command( Resources.TEXT_RESET_HOF, Command.SCREEN, 2 );
    
    private final MobileDrome mDrome;
    private Form mForm;
    
    private RecordStore mStoreScoresLevel;
    private RecordStore mStoreScoresTotal;
    private RecordStore mStoreRemovesLevel;
    
    public MobileHallOfFame( final MobileDrome pDrome )
    {
        super();
        mDrome = pDrome;
        readFromStore();
    }
    
    public final boolean addScoreLevel( final short pScore )
    {
        final boolean added = super.addScoreLevel( pScore );
        if ( added )
        {
            writeToStoreShort( STORE_SCORES_LEVEL, mScoresLevel.getValues() );
        }
        return added;
    }
    
    public final boolean addScoreTotal( final short pScore )
    {
        final boolean added = super.addScoreTotal( pScore );
        if ( added )
        {
            writeToStoreShort( STORE_SCORES_TOTAL, mScoresTotal.getValues() );
        }
        return added;
    }
    
    public final boolean addRemovesLevel( final byte pCount )
    {
        final boolean added = super.addRemovesLevel( pCount );
        if ( added )
        {
            writeToStoreByte( STORE_REMOVES_LEVEL, mRemovesLevel.getValues() );
        }
        return added;
    }
    
    private final void readFromStore()
    {
        readScoresLevel();
        readScoresTotal();
        readRemovesLevel();
    }
    
    private final void writeToStore()
    {
        writeToStoreShort( STORE_SCORES_LEVEL, mScoresLevel.getValues() );
        writeToStoreShort( STORE_SCORES_TOTAL, mScoresTotal.getValues() );
        writeToStoreByte( STORE_REMOVES_LEVEL, mRemovesLevel.getValues() );
    }
    
    private static final boolean addRecordToStore( final RecordStore pStore,
                                                   final byte[] pData )
    {
        boolean success = false;
        
        try
        {
            pStore.addRecord( pData, 0, pData.length );
            success = true;
        }
        catch ( final RecordStoreException e )
        {
            MobileMain.error( "Failed to add record to store " + pStore, e );
        }
        
        return success;
    }
    
    private static final RecordStore openStore( final String pStoreName )
    {
        RecordStore store = null;
        try
        {
            store = RecordStore.openRecordStore( pStoreName, false );
        }
        catch ( final RecordStoreException e )
        {
            MobileMain.error( "Failed to open store " + pStoreName, e );
            closeStore( store );
            store = null;
        }
        return store;
    }
    
    private static final void closeStore( final RecordStore pStore )
    {
        if ( pStore != null )
        {
            try
            {
                pStore.closeRecordStore();
            }
            catch ( final RecordStoreException e )
            {
                MobileMain.error( "Failed to close store " + pStore, e );
            }
        }
    }
    
    private static final boolean writeToStoreShort( final String pStoreName,
                                                    final short[] pValues )
    {
        boolean success = false;
        
        final RecordStore store = openStore( pStoreName );
        if ( store != null )
        {
            try
            {
                for ( int i = 0; i < pValues.length; i++ )
                {
                    final byte[] data = getDataShort( pValues[ i ] );
                    store.setRecord( i + 1, data, 0, data.length );
                }
                success = true;
            }
            catch ( final IOException e )
            {
                MobileMain.error( "Failed to get short data", e );
            }
            catch ( final RecordStoreException e )
            {
                MobileMain.error(   "Failed to write short data to store "
                                  + pStoreName, e );
            }
            finally
            {
                closeStore( store );
            }
        }
        
        return success;
    }
    
    private static final boolean writeToStoreByte( final String pStoreName,
                                                   final byte[] pValues )
    {
        boolean success = false;
        
        final RecordStore store = openStore( pStoreName );
        if ( store != null )
        {
            try
            {
                for ( int i = 0; i < pValues.length; i++ )
                {
                    final byte[] data = getDataByte( pValues[ i ] );
                    store.setRecord( i + 1, data, 0, data.length );
                }
                success = true;
            }
            catch ( final IOException e )
            {
                MobileMain.error( "Failed to get byte data", e );
            }
            catch ( final RecordStoreException e )
            {
                MobileMain.error(   "Failed to write byte data to store "
                                  + pStoreName, e );
            }
            finally
            {
                closeStore( store );
            }
        }
        
        return success;
    }
    
    private static final boolean initializeStoreShort( final RecordStore pStore,
                                                       final short[] pValues )
    {
        boolean success = true;
        try
        {
            for ( int i = 0; success && i < pValues.length; i++ )
            {
                success = addRecordToStore( pStore, getDataShort( pValues[ i ] ) );
            }
        }
        catch ( final IOException e )
        {
            MobileMain.error( "Failed to get short data", e );
            success = false;
        }
        return success;
    }
    
    private static final boolean initializeStoreByte( final RecordStore pStore,
                                                      final byte[] pValues )
    {
        boolean success = true;
        try
        {
            for ( int i = 0; success && i < pValues.length; i++ )
            {
                success = addRecordToStore( pStore, getDataByte( pValues[ i ] ) );
            }
        }
        catch ( final IOException e )
        {
            MobileMain.error( "Failed to get byte data", e );
            success = false;
        }
        return success;
    }
    
    private static final byte[] getDataShort( final short pValue )
        throws IOException
    {
        final byte[] data;
        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStream dos = new DataOutputStream( baos );
        dos.writeShort( pValue );
        data = baos.toByteArray();
        dos.close();
        
        return data;
    }
    
    private static final byte[] getDataByte( final byte pValue )
        throws IOException
    {
        final byte[] data;
    
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStream dos = new DataOutputStream( baos );
        dos.writeByte( pValue );
        data = baos.toByteArray();
        dos.close();
    
        return data;
    }
    
    public void clearAll()
    {
        super.clearAll();
        writeToStore();
    }
    
    public void showHallOfFame()
    {
        final Screen scoreScreen = getHOFScreen();
        scoreScreen.addCommand( mCmdOK );
        scoreScreen.addCommand( mCmdReset );
        scoreScreen.setCommandListener( this );
        mDrome.getDisplay()
              .setCurrent( scoreScreen );
    }
    
    private Screen getHOFScreen()
    {
        updateForm();
        return mForm;
    }
    
    private void updateForm()
    {
        mForm = new Form( Resources.TITLE_HOF );
        addScores();
        addRemoves();
    }

    public final void commandAction( final Command pC, final Displayable pD )
    {
        if ( pC == mCmdOK )
        {
            mDrome.show();
        }
        else if ( pC == mCmdReset )
        {
            clearAll();
            showHallOfFame();
        }
    }
    
    private final void addScores()
    {
        addTitle( mForm, Resources.TITLE_SCORES_TOTAL );
        addValues( mForm, mScoresTotal.getValues() );
        addTitle( mForm, Resources.TITLE_SCORES_LEVEL );
        addValues( mForm, mScoresLevel.getValues() );
    }
    
    private final void addRemoves()
    {
        addTitle( mForm, Resources.TITLE_REMOVES_LEVEL );
        addValues( mForm, mRemovesLevel.getValues() );
    }
    
    private static final void addTitle( final Form pForm,
                                        final String pTitle )
    {
        pForm.append( pTitle + "\n" );
    }

    private static final void addValues( final Form pForm,
                                         final short[] pValues )
    {
        final int size = pValues.length;
        for( int i = 0; i < size; i++ )
        {
            addItem( pForm,
                     String.valueOf( i + 1 ),
                     String.valueOf( pValues[ i ] ),
                     true );
        }
    }
    
    private static final void addValues( final Form pForm,
                                         final byte[] pValues )
    {
        final int size = pValues.length;
        for( int i = 0; i < size; i++ )
        {
            addItem( pForm,
                     String.valueOf( i + 1 ),
                     String.valueOf( pValues[ i ] ),
                     true );
        }
    }
    
    private static final void addItem( final Form pForm,
                                       final String pRank,
                                       final String pValue,
                                       final boolean pRowChange )
    {
        pForm.append( pRank + ":" );
        pForm.append( pValue + ( pRowChange ? "\n" : "" ) );
    }
    
    private final void readScoresLevel()
    {
        try
        {
            mStoreScoresLevel =
                RecordStore.openRecordStore( STORE_SCORES_LEVEL, true );
            if ( mStoreScoresLevel.getNumRecords () == 0 )
            {
                initializeStoreShort( mStoreScoresLevel, mScoresLevel.getValues() );
            }
            else
            {
                mScoresLevel.setValues( readValuesShort( mStoreScoresLevel,
                                                         mScoresLevel.size() ) );
            }
        }        
        catch ( final RecordStoreException e )
        {
            MobileMain.error(   "Failed to read from store "
                              + STORE_SCORES_LEVEL, e );
        }
        
        try
        {
            mStoreScoresLevel.closeRecordStore();
        }
        catch ( final RecordStoreException e )
        {
            MobileMain.error(   "Failed to close store "
                              + STORE_SCORES_LEVEL, e );
        }
        finally
        {
            mStoreScoresLevel = null;
        }
    }
    
    private final void readScoresTotal()
    {
        try
        {
            mStoreScoresTotal =
                RecordStore.openRecordStore( STORE_SCORES_TOTAL, true );
            if ( mStoreScoresTotal.getNumRecords () == 0 )
            {
                initializeStoreShort( mStoreScoresTotal, mScoresTotal.getValues() );
            }
            else
            {
                mScoresTotal.setValues( readValuesShort( mStoreScoresTotal,
                                                         mScoresTotal.size() ) );
            }
        }        
        catch ( final RecordStoreException e )
        {
            MobileMain.error(   "Failed to read from store "
                              + STORE_SCORES_TOTAL, e );
        }
        
        try
        {
            mStoreScoresTotal.closeRecordStore();
        }
        catch ( final RecordStoreException e )
        {
            MobileMain.error(   "Failed to close store "
                              + STORE_SCORES_TOTAL, e );
        }
        finally
        {
            mStoreScoresTotal = null;
        }
    }
    
    private final void readRemovesLevel()
    {
        try
        {
            mStoreRemovesLevel =
                RecordStore.openRecordStore( STORE_REMOVES_LEVEL, true );
            if ( mStoreRemovesLevel.getNumRecords () == 0 )
            {
                initializeStoreByte( mStoreRemovesLevel, mRemovesLevel.getValues() );
            }
            else
            {
                mRemovesLevel.setValues( readValuesByte( mStoreRemovesLevel,
                                                         mRemovesLevel.size() ) );
            }
        }        
        catch ( final RecordStoreException e )
        {
            MobileMain.error(   "Failed to read from store "
                              + STORE_REMOVES_LEVEL, e );
        }
        
        try
        {
            mStoreRemovesLevel.closeRecordStore();
        }
        catch ( final RecordStoreException e )
        {
            MobileMain.error(   "Failed to close store "
                              + STORE_REMOVES_LEVEL, e );
        }
        finally
        {
            mStoreRemovesLevel = null;
        }
    }
    
    private static final short[] readValuesShort( final RecordStore pStore,
                                                  final int pValueCount )
        throws RecordStoreException
    {
        short[] values = new short[ pValueCount ];
        
        for ( int i = 0; i < values.length; i++ )
        {
           final DataInputStream dis = getDataStream( pStore, i + 1 );
           if ( dis != null )
           {
               try
               {
                   values[ i ] = dis.readShort();
                   dis.close();
               }
               catch ( final IOException e )
               {
                   MobileMain.error(   "Failed to read short values from store "
                                     + pStore, e );
               }
            }
        }
        
        return values;
    }
    
    
    private static final byte[] readValuesByte( final RecordStore pStore,
                                                final int pValueCount )
        throws RecordStoreException
    {
        byte[] values = new byte[ pValueCount ];
        
        for ( int i = 0; i < values.length; i++ )
        {
           final DataInputStream dis = getDataStream( pStore, i + 1 );
           if ( dis != null )
           {
               try
               {
                   values[ i ] = dis.readByte();
                   dis.close();
               }
               catch ( final IOException e )
               {
                   MobileMain.error(   "Failed to read byte values from store "
                                     + pStore, e );
               }
            }
        }
        
        return values;
    }
    
    private static final DataInputStream getDataStream( final RecordStore pStore,
                                                        final int pRecordId )
        throws RecordStoreException
    {
       DataInputStream dis = null;
        
        final byte[] data = pStore.getRecord( pRecordId );
        if ( data != null )
        {
           dis = new DataInputStream( new ByteArrayInputStream( data ) );
        }
        
        return dis;
    }
}
