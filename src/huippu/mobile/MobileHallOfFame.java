package huippu.mobile;

import huippu.common.HallOfFame;
import huippu.common.Resources;
import huippu.common.Score;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Screen;
import javax.microedition.lcdui.StringItem;
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
    private final int mScoreWidth;
    private final int mLevelWidth;
    private Form mForm;
    
    private RecordStore mStoreScoresLevel;
    private RecordStore mStoreScoresTotal;
    private RecordStore mStoreRemovesLevel;
    
    public MobileHallOfFame( final MobileDrome pDrome, final Font pFont )
    {
        super();
        mDrome = pDrome;
        final int charWidth = pFont.charWidth( '0' );
        mScoreWidth = 5 * charWidth;
        mLevelWidth = 3 * charWidth;
        readFromStore();
    }
    
    public final boolean addScoreLevel( final Score pScore )
    {
        final boolean added = super.addScoreLevel( pScore );
        if ( added )
        {
            writeToStoreInt( STORE_SCORES_LEVEL, mScoresLevel.getValues() );
        }
        return added;
    }
    
    public final boolean addScoreTotal( final Score pScore )
    {
        final boolean added = super.addScoreTotal( pScore );
        if ( added )
        {
            writeToStoreInt( STORE_SCORES_TOTAL, mScoresTotal.getValues() );
        }
        return added;
    }
    
    public final boolean addRemovesLevel( final Score pScore )
    {
        final boolean added = super.addRemovesLevel( pScore );
        if ( added )
        {
            writeToStoreInt( STORE_REMOVES_LEVEL, mRemovesLevel.getValues() );
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
        writeToStoreInt( STORE_SCORES_LEVEL, mScoresLevel.getValues() );
        writeToStoreInt( STORE_SCORES_TOTAL, mScoresTotal.getValues() );
        writeToStoreInt( STORE_REMOVES_LEVEL, mRemovesLevel.getValues() );
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
    
    private static final boolean writeToStoreInt( final String pStoreName,
                                                  final Score[] pValues )
    {
        boolean success = false;
        
        final RecordStore store = openStore( pStoreName );
        if ( store != null )
        {
            try
            {
                for ( int i = 0; i < pValues.length; i++ )
                {
                    final byte[] data = pValues[ i ].getAsBytes();
                    store.setRecord( i + 1, data, 0, data.length );
                }
                success = true;
            }
            catch ( final IOException e )
            {
                MobileMain.error( "Failed to get int data", e );
            }
            catch ( final RecordStoreException e )
            {
                MobileMain.error(   "Failed to write int data to store "
                                  + pStoreName, e );
            }
            finally
            {
                closeStore( store );
            }
        }
        
        return success;
    }
    
    private static final boolean initializeStoreInt( final RecordStore pStore,
                                                     final Score[] pValues )
    {
        boolean success = true;
        try
        {
            for ( int i = 0; success && i < pValues.length; i++ )
            {
                success = addRecordToStore( pStore, pValues[ i ].getAsBytes() );
            }
        }
        catch ( final IOException e )
        {
            MobileMain.error( "Failed to get int data", e );
            success = false;
        }
        return success;
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
        addTitle( Resources.TITLE_SCORES_TOTAL );
        addValues( mScoresTotal.getValues() );
        addTitle( Resources.TITLE_SCORES_LEVEL );
        addValues( mScoresLevel.getValues() );
    }
    
    private final void addRemoves()
    {
        addTitle( Resources.TITLE_REMOVES_LEVEL );
        addValues( mRemovesLevel.getValues() );
    }
    
    private final void addTitle( final String pTitle )
    {
        mForm.append( pTitle + "\n" );
    }

    private final void addValues( final Score[] pValues )
    {
        final int size = pValues.length;
        for( int i = 0; i < size; i++ )
        {
            addItem( String.valueOf( i + 1 ), pValues[ i ] );
        }
    }
    
    private final void addItem( final String pRank, final Score pScore )
    {
        final int value = pScore.getValue();
        if ( value == 0 )
        {
            mForm.append( pRank + ":\n" );
        }
        else
        {
            mForm.append( pRank + ":" );
            
            final StringItem valueItem =
                new StringItem( null, String.valueOf( value ) );
            valueItem.setPreferredSize( mScoreWidth, -1 );
            mForm.append( valueItem );

            final StringItem levelItem =
                new StringItem( null, String.valueOf( pScore.getLevel() ) );
            levelItem.setPreferredSize( mLevelWidth, -1 );
            mForm.append( levelItem );
            
            mForm.append( String.valueOf( pScore.getDate() ) + "\n" );
        }
    }
    
    private final void readScoresLevel()
    {
        try
        {
            mStoreScoresLevel =
                RecordStore.openRecordStore( STORE_SCORES_LEVEL, true );
            if ( mStoreScoresLevel.getNumRecords () == 0 )
            {
                initializeStoreInt( mStoreScoresLevel, mScoresLevel.getValues() );
            }
            else
            {
                mScoresLevel.setValues( readValuesInt( mStoreScoresLevel,
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
                initializeStoreInt( mStoreScoresTotal, mScoresTotal.getValues() );
            }
            else
            {
                mScoresTotal.setValues( readValuesInt( mStoreScoresTotal,
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
                initializeStoreInt( mStoreRemovesLevel, mRemovesLevel.getValues() );
            }
            else
            {
                mRemovesLevel.setValues( readValuesInt( mStoreRemovesLevel,
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
    
    private static final Score[] readValuesInt( final RecordStore pStore,
                                                final int pValueCount )
        throws RecordStoreException
    {
        final Score[] values = new Score[ pValueCount ];
        
        for ( int i = 0; i < values.length; i++ )
        {
           final DataInputStream dis = getDataStream( pStore, i + 1 );
           if ( dis != null )
           {
               try
               {
                   values[ i ] = new Score( dis );
                   dis.close();
               }
               catch ( final IOException e )
               {
                   MobileMain.error(   "Failed to read scores from store "
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
