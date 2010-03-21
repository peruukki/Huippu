package huippu.mobile;

import huippu.common.HallOfFame;
import huippu.common.Resources;
import huippu.common.Score;
import huippu.common.ScoresInt;

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
    
    public MobileHallOfFame( final MobileDrome pDrome, final Font pFont )
    {
        super();
        mDrome = pDrome;
        final int charWidth = pFont.charWidth( '0' );
        mScoreWidth = 5 * charWidth;
        mLevelWidth = 3 * charWidth;
        readFromStore();
    }
    
    public final void addScoreTotal( final Score pScore )
    {
        if ( super.addScoreTotalBiggest( pScore ) )
        {
            writeToStore( STORE_SCORES_TOTAL_BIGGEST, mScoresTotalBiggest.getValues() );
        }
        if ( super.addScoreTotalSmallest( pScore ) )
        {
            writeToStore( STORE_SCORES_TOTAL_SMALLEST, mScoresTotalSmallest.getValues() );
        }
    }
    
    public final void addScoreLevel( final Score pScore )
    {
        if ( super.addScoreLevelBiggest( pScore ) )
        {
            writeToStore( STORE_SCORES_LEVEL_BIGGEST, mScoresLevelBiggest.getValues() );
        }
        if ( super.addScoreLevelSmallest( pScore ) )
        {
            writeToStore( STORE_SCORES_LEVEL_SMALLEST, mScoresLevelSmallest.getValues() );
        }
    }
    
    public final void addRemovesLevel( final Score pScore )
    {
        if ( super.addRemovesLevelSmallest( pScore ) )
        {
            writeToStore( STORE_REMOVES_LEVEL_SMALLEST, mRemovesLevelSmallest.getValues() );
        }
        if ( super.addRemovesLevelBiggest( pScore ) )
        {
            writeToStore( STORE_REMOVES_LEVEL_BIGGEST, mRemovesLevelBiggest.getValues() );
        }
    }

    public final void addRemove( final Score pScore )
    {
        if ( super.addRemoveBiggest( pScore ) )
        {
            writeToStore( STORE_REMOVES_BIGGEST, mRemovesBiggest.getValues() );
        }
    }
    
    private final void readFromStore()
    {
        readScores( STORE_SCORES_TOTAL_BIGGEST, mScoresTotalBiggest );
        readScores( STORE_SCORES_TOTAL_SMALLEST, mScoresTotalSmallest );
        readScores( STORE_SCORES_LEVEL_BIGGEST, mScoresLevelBiggest );
        readScores( STORE_SCORES_LEVEL_SMALLEST, mScoresLevelSmallest );
        readScores( STORE_REMOVES_LEVEL_SMALLEST, mRemovesLevelSmallest );
        readScores( STORE_REMOVES_LEVEL_BIGGEST, mRemovesLevelBiggest );
        readScores( STORE_REMOVES_BIGGEST, mRemovesBiggest );
    }
    
    private final void writeToStore()
    {
        writeToStore( STORE_SCORES_TOTAL_BIGGEST, mScoresTotalBiggest.getValues() );
        writeToStore( STORE_SCORES_TOTAL_SMALLEST, mScoresTotalSmallest.getValues() );
        writeToStore( STORE_SCORES_LEVEL_BIGGEST, mScoresLevelBiggest.getValues() );
        writeToStore( STORE_SCORES_LEVEL_SMALLEST, mScoresLevelSmallest.getValues() );
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
        addTitle( Resources.TITLE_SCORES_TOTAL_BIGGEST );
        addValues( mScoresTotalBiggest.getValues() );
        addTitle( Resources.TITLE_SCORES_TOTAL_SMALLEST );
        addValues( mScoresTotalSmallest.getValues() );
        addTitle( Resources.TITLE_SCORES_LEVEL_BIGGEST );
        addValues( mScoresLevelBiggest.getValues() );
        addTitle( Resources.TITLE_SCORES_LEVEL_SMALLEST );
        addValues( mScoresLevelSmallest.getValues() );
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
    
    private static final void readScores( final String pStoreName,
                                          final ScoresInt pScores )
    {
        final RecordStore store = MobileStorable.openStore( pStoreName, true, false );
        if ( store != null )
        {
            try
            {
                if ( store.getNumRecords () == 0 )
                {
                    initializeStore( store, pScores.getValues() );
                }
                else
                {
                    pScores.setValues( readScores( store, pScores.size() ) );
                }
            }        
            catch ( final RecordStoreException e )
            {
                MobileMain.error(   "Failed to read from store "
                                  + pStoreName, e );
            }
        }
        
        MobileStorable.closeStore( store );
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
