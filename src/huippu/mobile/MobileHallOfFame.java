package huippu.mobile;

import huippu.common.HallOfFame;
import huippu.common.Resources;
import huippu.common.Score;

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
    implements CommandListener, ConfirmationReceiver
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
            writeToStore( STORE_SCORES_LEVEL, mScoresLevel.getValues() );
        }
        return added;
    }
    
    public final boolean addScoreTotal( final Score pScore )
    {
        final boolean added = super.addScoreTotal( pScore );
        if ( added )
        {
            writeToStore( STORE_SCORES_TOTAL, mScoresTotal.getValues() );
        }
        return added;
    }
    
    public final boolean addRemovesLevel( final Score pScore )
    {
        final boolean added = super.addRemovesLevel( pScore );
        if ( added )
        {
            writeToStore( STORE_REMOVES_LEVEL, mRemovesLevel.getValues() );
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
        writeToStore( STORE_SCORES_LEVEL, mScoresLevel.getValues() );
        writeToStore( STORE_SCORES_TOTAL, mScoresTotal.getValues() );
        writeToStore( STORE_REMOVES_LEVEL, mRemovesLevel.getValues() );
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
    
    private static final boolean writeToStore( final String pStoreName,
                                               final Score[] pValues )
    {
        boolean success = false;
        
        final RecordStore store = MobileStorable.openStore( pStoreName, false, true );
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
                MobileMain.error( "Failed to get score data", e );
            }
            catch ( final RecordStoreException e )
            {
                MobileMain.error(   "Failed to write score data to store "
                                  + pStoreName, e );
            }
            finally
            {
                MobileStorable.closeStore( store );
            }
        }
        
        return success;
    }
    
    private static final boolean initializeStore( final RecordStore pStore,
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
            MobileMain.error( "Failed to get score data", e );
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
        mStoreScoresLevel =
            MobileStorable.openStore( STORE_SCORES_LEVEL, true, false );
        if ( mStoreScoresLevel != null )
        {
            try
            {
                if ( mStoreScoresLevel.getNumRecords () == 0 )
                {
                    initializeStore( mStoreScoresLevel, mScoresLevel.getValues() );
                }
                else
                {
                    mScoresLevel.setValues( readScores( mStoreScoresLevel,
                                            mScoresLevel.size() ) );
                }
            }        
            catch ( final RecordStoreException e )
            {
                MobileMain.error(   "Failed to read from store "
                                  + STORE_SCORES_LEVEL, e );
            }
        }
        
        MobileStorable.closeStore( mStoreScoresLevel );
        mStoreScoresLevel = null;
    }
    
    private final void readScoresTotal()
    {
        mStoreScoresTotal =
            MobileStorable.openStore( STORE_SCORES_TOTAL, true, false );
        if ( mStoreScoresTotal != null )
        {
            try
            {
                if ( mStoreScoresTotal.getNumRecords () == 0 )
                {
                    initializeStore( mStoreScoresTotal, mScoresTotal.getValues() );
                }
                else
                {
                    mScoresTotal.setValues( readScores( mStoreScoresTotal,
                                                        mScoresTotal.size() ) );
                }
            }        
            catch ( final RecordStoreException e )
            {
                MobileMain.error(   "Failed to read from store "
                                  + STORE_SCORES_TOTAL, e );
            }
        }
        
        MobileStorable.closeStore( mStoreScoresTotal );
        mStoreScoresTotal = null;
    }
    
    private final void readRemovesLevel()
    {
        mStoreRemovesLevel =
            MobileStorable.openStore( STORE_REMOVES_LEVEL, true, false );
        if ( mStoreRemovesLevel != null )
        {
            try
            {
                if ( mStoreRemovesLevel.getNumRecords () == 0 )
                {
                    initializeStore( mStoreRemovesLevel, mRemovesLevel.getValues() );
                }
                else
                {
                    mRemovesLevel.setValues( readScores( mStoreRemovesLevel,
                                                         mRemovesLevel.size() ) );
                }
            }        
            catch ( final RecordStoreException e )
            {
                MobileMain.error(   "Failed to read from store "
                                  + STORE_REMOVES_LEVEL, e );
            }
        }
        
        MobileStorable.closeStore( mStoreRemovesLevel );
        mStoreRemovesLevel = null;
    }
    
    private static final Score[] readScores( final RecordStore pStore,
                                             final int pValueCount )
        throws RecordStoreException
    {
        final Score[] values = new Score[ pValueCount ];
        
        for ( int i = 0; i < values.length; i++ )
        {
           final DataInputStream dis = MobileStorable.getDataStream( pStore, i + 1 );
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

    public final void commandAction( final Command pCommand,
                                     final Displayable pDisplayable )
    {
        if ( pCommand == mCmdOK )
        {
            mDrome.show();
        }
        else if ( pCommand == mCmdReset )
        {
            MobileMain.confirm( Resources.TEXT_RESET_HOF_CONFIRM, this, pCommand );
        }
        else
        {
            MobileMain.error(   "Unknown action command for class "
                              + this.getClass() + ": " + pCommand, null );
        }
    }

    public final void handleResult( final Command pCommand, final int pResult)
    {
        if ( pCommand == mCmdReset )
        {
            if ( pResult == Command.OK )
            {
                clearAll();
                showHallOfFame();
            }
        }
        else
        {
            MobileMain.error(   "Unknown confirmation command for class "
                              + this.getClass() + ": " + pCommand, null );
        }
    }
}
