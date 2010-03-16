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
    private RecordStore mStoreRemovesLevelSmallest;
    private RecordStore mStoreRemovesLevelBiggest;
    private RecordStore mStoreRemovesBiggest;
    
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
    
    public final boolean addRemovesLevelSmallest( final Score pScore )
    {
        final boolean added = super.addRemovesLevelSmallest( pScore );
        if ( added )
        {
            writeToStore( STORE_REMOVES_LEVEL_SMALLEST, mRemovesLevelSmallest.getValues() );
        }
        return added;
    }
    
    public final boolean addRemovesLevelBiggest( final Score pScore )
    {
        final boolean added = super.addRemovesLevelBiggest( pScore );
        if ( added )
        {
            writeToStore( STORE_REMOVES_LEVEL_BIGGEST, mRemovesLevelBiggest.getValues() );
        }
        return added;
    }

    public final boolean addRemoveBiggest( final Score pScore )
    {
        final boolean added = super.addRemoveBiggest( pScore );
        if ( added )
        {
            writeToStore( STORE_REMOVES_BIGGEST, mRemovesBiggest.getValues() );
        }
        return added;
    }
    
    private final void readFromStore()
    {
        readScoresLevel();
        readScoresTotal();
        readRemovesLevelSmallest();
        readRemovesLevelBiggest();
        readRemovesBiggest();
    }
    
    private final void writeToStore()
    {
        writeToStore( STORE_SCORES_LEVEL, mScoresLevel.getValues() );
        writeToStore( STORE_SCORES_TOTAL, mScoresTotal.getValues() );
        writeToStore( STORE_REMOVES_LEVEL_SMALLEST, mRemovesLevelSmallest.getValues() );
        writeToStore( STORE_REMOVES_LEVEL_BIGGEST, mRemovesLevelBiggest.getValues() );
        writeToStore( STORE_REMOVES_BIGGEST, mRemovesBiggest.getValues() );
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
        addTitle( Resources.TITLE_REMOVES_LEVEL_SMALLEST );
        addValues( mRemovesLevelSmallest.getValues() );
        addTitle( Resources.TITLE_REMOVES_LEVEL_BIGGEST );
        addValues( mRemovesLevelBiggest.getValues() );
        addTitle( Resources.TITLE_REMOVES_BIGGEST );
        addValues( mRemovesBiggest.getValues() );
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
    
    private final void readRemovesLevelSmallest()
    {
        mStoreRemovesLevelSmallest =
            MobileStorable.openStore( STORE_REMOVES_LEVEL_SMALLEST, true, false );
        if ( mStoreRemovesLevelSmallest != null )
        {
            try
            {
                if ( mStoreRemovesLevelSmallest.getNumRecords () == 0 )
                {
                    initializeStore( mStoreRemovesLevelSmallest,
                                     mRemovesLevelSmallest.getValues() );
                }
                else
                {
                    mRemovesLevelSmallest.setValues(
                        readScores( mStoreRemovesLevelSmallest,
                                    mRemovesLevelSmallest.size() ) );
                }
            }        
            catch ( final RecordStoreException e )
            {
                MobileMain.error(   "Failed to read from store "
                                  + STORE_REMOVES_LEVEL_SMALLEST, e );
            }
        }
        
        MobileStorable.closeStore( mStoreRemovesLevelSmallest );
        mStoreRemovesLevelSmallest = null;
    }
    
    private final void readRemovesLevelBiggest()
    {
        mStoreRemovesLevelBiggest =
            MobileStorable.openStore( STORE_REMOVES_LEVEL_BIGGEST, true, false );
        if ( mStoreRemovesLevelBiggest != null )
        {
            try
            {
                if ( mStoreRemovesLevelBiggest.getNumRecords () == 0 )
                {
                    initializeStore( mStoreRemovesLevelBiggest,
                                     mRemovesLevelBiggest.getValues() );
                }
                else
                {
                    mRemovesLevelBiggest.setValues(
                        readScores( mStoreRemovesLevelBiggest,
                                    mRemovesLevelBiggest.size() ) );
                }
            }        
            catch ( final RecordStoreException e )
            {
                MobileMain.error(   "Failed to read from store "
                                  + STORE_REMOVES_LEVEL_BIGGEST, e );
            }
        }
        
        MobileStorable.closeStore( mStoreRemovesLevelBiggest );
        mStoreRemovesLevelBiggest = null;
    }
    
    private final void readRemovesBiggest()
    {
        mStoreRemovesBiggest =
            MobileStorable.openStore( STORE_REMOVES_BIGGEST, true, false );
        if ( mStoreRemovesBiggest != null )
        {
            try
            {
                if ( mStoreRemovesBiggest.getNumRecords () == 0 )
                {
                    initializeStore( mStoreRemovesBiggest, mRemovesBiggest.getValues() );
                }
                else
                {
                    mRemovesBiggest.setValues( readScores( mStoreRemovesBiggest,
                                                           mRemovesBiggest.size() ) );
                }
            }        
            catch ( final RecordStoreException e )
            {
                MobileMain.error(   "Failed to read from store "
                                  + STORE_REMOVES_BIGGEST, e );
            }
        }
        
        MobileStorable.closeStore( mStoreRemovesBiggest );
        mStoreRemovesBiggest = null;
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
