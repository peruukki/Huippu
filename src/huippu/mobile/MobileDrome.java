package huippu.mobile;

import huippu.common.Cell;
import huippu.common.DromeComponent;
import huippu.common.Dude;
import huippu.common.DudeGrid;
import huippu.common.GameState;
import huippu.common.Pointer;
import huippu.common.Resources;
import huippu.common.Score;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

final class MobileDrome
	extends Canvas
	implements Runnable
{
    private static final String STORE_GAME_STATE = "GameState";
    
    private static final int TEXT_OFFSET = 2;
    
    private static final Font FONT =
        Font.getFont( Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL );

    private static final Font FONT_BOLD_LARGE =
        Font.getFont( Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM );
    
    private static final int TEXT_HEIGHT = FONT.getHeight() + 2;
    private static final int REDUCE_WIDTH = 0;  // 52
    private static final int REDUCE_HEIGHT = 0; // 75
    
    private int mColumnCount = 11;
    private int mRowCount = 7;

    private int mDromeOffsetX = 0;
    private int mDromeOffsetY = 0;

    private int mCellWidth;
    private int mCellHeight;
    
    private int mScreenWidth;
    private int mScreenHeight;
    
    private int mDromeWidth;
    private int mDromeHeight;
    
    private final MobileHallOfFame mHOF = new MobileHallOfFame( this, FONT );
    
    private final Display mDisplay;
    private final MobileMenu mMenu;
    private final GameState mState;
    
    private String mFinishedString = null;
    private boolean mFinished = false;
    
    private String mRemoveCountString = null;
    private MobileTextBox mRemoveCountText = null;
    private Timer mRemoveCountTimer = new Timer();
    private TimerTask mRemoveCountTimerTask = null;
    private static final int REMOVE_COUNT_SHOW_MIN = 5;
    private static final int REMOVE_COUNT_TIMER_DELAY_MS = 1000;
        
    private final Object mSync = new Object();
	
    public MobileDrome( final MobileMain pApplication )
    {
        mDisplay = Display.getDisplay( pApplication );
        mMenu = new MobileMenu( pApplication, this );
        DromeComponent.setGridSize( mColumnCount, mRowCount );
        mState = getInitialState();
        setFullScreenMode( true );
        reinit();
        sizeChanged( getWidth(), getHeight() );
    }
    
    private final GameState getInitialState()
    {
        GameState state = null;
        
        // Try to read stored game state first
        RecordStore store = MobileStorable.openStore( STORE_GAME_STATE, false, false );
        if ( store != null )
        {
            try
            {
                final DataInputStream data = MobileStorable.getDataStream( store, 1 );
                final MobileDudeGrid dudeGrid = new MobileDudeGrid( data );
                final MobilePointer pointer = new MobilePointer( data );
                state = new GameState( dudeGrid, pointer, data );
            }
            catch ( final RecordStoreException e )
            {
                MobileMain.error( "Failed to read from game state store", e );
            }
            catch ( final IOException e )
            {
                MobileMain.error( "Failed to read from game state store", e );
            }
            finally
            {
                MobileStorable.closeStore( store );
                MobileStorable.deleteStore( STORE_GAME_STATE );
            }
        }
        else
        {
            // Create new uninitialized DudeGrid
            state = new GameState( new MobileDudeGrid( mColumnCount,
                                                       mRowCount ),
                                   new MobilePointer() );
        }
        
        return state;
    }
    
    private final void reinit()
    {
        mRemoveCountString = null;
        mFinishedString = null;
        mFinished = false;
        mMenu.setNextLevelEnabled( false );
        
        if ( mState.isRestored() )
        {
            mState.clearRestored();
            if ( mState.getDudeGrid()
                       .allDudesRemoved() )
            {
                dromeFinished( true, true );
            }
        }
        else
        {
            initPointer();
            initDudes();
            mState.setScoreLevel( 0 );
            mState.setRemoveCountLevel( 0 );
        }
        mState.getDudeGrid()
              .setCurrentCell( mState.getPointer()
                                     .getCell() );
    }
    
    protected final void sizeChanged( final int pWidth, final int pHeight )
    {
        initDrome( pWidth - REDUCE_WIDTH, pHeight - REDUCE_HEIGHT );
        DromeComponent.setCellSize( mCellWidth, mCellHeight );
        Dude.updateSize();
        mState.getPointer()
              .updateScreenPosition();
        mState.getDudeGrid()
              .updateDudePositions();
    }
    
    private final void initDrome( final int pWidth, final int pHeight )
    {
        mScreenWidth = pWidth;
        mScreenHeight = pHeight;
        
        mDromeWidth = round( pWidth, mColumnCount );
        mDromeHeight = round( pHeight - 2 * TEXT_HEIGHT, mRowCount );
        
        mDromeOffsetX = ( mScreenWidth - mDromeWidth ) / 2;
        mDromeOffsetY = 1;
        
        mCellWidth = mDromeWidth / mColumnCount;
        mCellHeight = mDromeHeight / mRowCount;
    }
    
    private final void initPointer()
    {
        mState.getPointer()
              .setCellPosition( new Cell( 0, 0 ) );
    }
        
    private final void initDudes()
    {
        final DudeGrid dudeGrid = mState.getDudeGrid(); 
        dudeGrid.fillWithDudes( mState.getLevel() );
    }

    /**
     * Rounds the given value to next lowest number divisible by the
     * cell size.
     *
     * @param value  number to be rounded down
     * @return rounded down value
     */
    private final int round( final int pValue, final int pCellCount  )
    {
        final int delta = pValue % pCellCount;
        return pValue - delta;
    }

    protected void paint( final Graphics pG )
    {
        if ( redrawAll( pG ) )
        {
            pG.setFont( FONT );
            drawScreen( pG );
            pG.translate( mDromeOffsetX, mDromeOffsetY );

            drawDudes( pG );
            mState.getPointer()
            .draw( pG );
            drawFinishedString( pG );

            pG.translate( -mDromeOffsetX, -mDromeOffsetY );
        }
        else
        {
            pG.setColor( Resources.COLOR_BG_OTHER );
            pG.fillRect( pG.getClipX(),
                         pG.getClipY(),
                         pG.getClipWidth(),
                         pG.getClipHeight() );
        }
    }
    
    private static final boolean redrawAll( final Graphics pG )
    {
        return ( pG.getClipX() == 0 );
    }

    private final void drawScreen( final Graphics pG )
    {
        // Clear background
        pG.setColor( Resources.COLOR_BG_OTHER );
        pG.fillRect( 0, 0, mScreenWidth, mScreenHeight );

        // Draw shadow
        pG.setColor( Resources.COLOR_SHADOW );
        pG.drawRect( mDromeOffsetX + 1,
                     mDromeOffsetY + 1,
                     mDromeWidth - 1,
                     mDromeHeight - 1 );
        
        // Fill drome with background color
        pG.setColor( Resources.COLOR_BG_DROME );
        pG.fillRect( mDromeOffsetX,
                     mDromeOffsetY,
                     mDromeWidth,
                     mDromeHeight );

        final int bottomY = mScreenHeight - 2;
        final int leftX = TEXT_OFFSET;
        final int rightX = mScreenWidth - TEXT_OFFSET;

        // Display level score
        drawString( pG,
                    Resources.TEXT_SCORE_LEVEL + mState.getScoreLevel(),
                    leftX,
                    bottomY - TEXT_HEIGHT,
                    Graphics.BOTTOM | Graphics.LEFT );
        
        // Display total score
        final int leftXRemoveCount = leftX
            + drawString( pG,
                          Resources.TEXT_SCORE_TOTAL + mState.getScoreTotal(),
                          leftX,
                          bottomY,
                          Graphics.BOTTOM | Graphics.LEFT );

        
        // Display screen size
//        drawString( pG,
//                      Resources.TEXT_SCREEN_SIZE + mDromeWidth
//                    + " x " + mDromeHeight,
//                    rightX,
//                    bottomY - 2 * TEXT_HEIGHT,
//                    Graphics.BOTTOM | Graphics.RIGHT );
        
        // Display level
        drawString( pG,
                    Resources.TEXT_LEVEL + mState.getLevel(),
                    rightX,
                    bottomY - TEXT_HEIGHT,
                    Graphics.BOTTOM | Graphics.RIGHT );
        
        // Display remove count
        final int rightXRemoveCount = rightX
            - drawString( pG,
                          Resources.TEXT_REMOVES_LEVEL + mState.getRemoveCountLevel(),
                          rightX,
                          bottomY,
                          Graphics.BOTTOM | Graphics.RIGHT );
        
        drawRemoveCountString( pG,
                                 leftXRemoveCount
                               + ( ( rightXRemoveCount - leftXRemoveCount ) / 2 ) );
    }
    
    private final void drawFinishedString( final Graphics pG )
    {
        if ( mFinishedString != null )
        {
            drawStringWithBox( pG,
                               mFinishedString,
                               mDromeOffsetX + ( mDromeWidth / 2 ),
                               mDromeOffsetY + ( mDromeHeight / 2 ) );
        }
    }
    
    private final synchronized void drawRemoveCountString( final Graphics pG,
                                                           final int pX )
    {
        if ( mRemoveCountString != null )
        {
            // Change to large font
            final Font font = pG.getFont();
            
            mRemoveCountText =
                new MobileTextBox( mRemoveCountString,
                                   pX,
                                     mDromeOffsetY
                                   + mDromeHeight
                                   + ( ( mScreenHeight - mDromeHeight ) / 2 ),
                                   FONT_BOLD_LARGE );
            mRemoveCountText.draw( pG );
            
            // Change back to previous font
            pG.setFont( font );
        }
    }
    
    private final void clearRemoveCountString()
    {
        synchronized ( mSync )
        {
            mRemoveCountString = null;
            mRemoveCountTimerTask = null;
            repaint( mRemoveCountText.getLeftX(),
                     mRemoveCountText.getTopY(),
                     mRemoveCountText.getWidth() + 1,
                     mRemoveCountText.getHeight() + 1 );
            mRemoveCountText = null;
        }
    }
    
    private static final int drawString( final Graphics pG, final String pString,
                                         final int pX, final int pY,
                                         final int pAnchor )
    {
        pG.setColor( Resources.COLOR_SHADOW );
        pG.drawString( pString, pX + 1, pY + 1, pAnchor );
        
        pG.setColor( Resources.COLOR_TEXT );
        pG.drawString( pString, pX, pY, pAnchor );
        
        return pG.getFont()
                 .stringWidth( pString );
    }
            
    private static final void drawStringWithBox( final Graphics pG,
                                                 final String pString,
                                                 final int pX, final int pY )
    {
        final Font font = pG.getFont();
        final int stringWidth = font.stringWidth( pString );
        final int stringHeight = font.getHeight();
        final int border = 5;
        final int bottomReduction = 2;
        final int leftX = pX - ( stringWidth / 2 ) - border;
        final int topY = pY - ( stringHeight / 2 ) - border;

        // Draw shadow
        pG.setColor( Resources.COLOR_SHADOW );
        pG.drawRect( leftX + 1,
                     topY + 1,
                     stringWidth + ( 2 * border ) - 2,
                     stringHeight + ( 2 * border ) - 2 - bottomReduction );

        // Draw background box
        pG.setColor( Resources.COLOR_BG_OTHER );
        pG.fillRect( leftX,
                     topY,
                     stringWidth + ( 2 * border ) - 1,
                     stringHeight + ( 2 * border ) - 1 - bottomReduction );

        // Draw message
        drawString( pG,
                    pString,
                    leftX + border,
                    topY + border - 1,
                    Graphics.TOP | Graphics.LEFT );
    }

    private final void drawDudes( final Graphics pG )
    {
        mState.getDudeGrid()
              .drawDudesAll( pG );
    }

    /**
     * Handle keyboard input.
     *
     * @param pKeyCode  pressed key is either Canvas arrow key (UP,
     *                  DOWN, LEFT, RIGHT) or simulated with KEY_NUM (2, 8, 4, 6).
     */
    public void keyPressed( final int pKeyCode )
    {
        int gameAction = getGameAction( pKeyCode );
        if ( gameAction == 0 )
        {
            switch (pKeyCode)
            {
                case Canvas.KEY_NUM2:
                    gameAction = Canvas.UP;
                    break;

                case Canvas.KEY_NUM8:
                    gameAction = Canvas.DOWN;
                    break;

                case Canvas.KEY_NUM4:
                    gameAction = Canvas.LEFT;
                    break;

                case Canvas.KEY_NUM6:
                    gameAction = Canvas.RIGHT;
                    break;

                case Canvas.KEY_NUM5:
                    gameAction = Canvas.FIRE;
                    break;

                default:
                    break;
            }
        }

        boolean repaint = true;
        boolean updateCurrentCell = true;
        boolean dromeFinished = false;
        boolean dromeFinishSuccess = false;
        final Pointer pointer = mState.getPointer();
        switch ( gameAction )
        {
            case Canvas.UP:
                pointer.moveUp( 1 );
                break;

            case Canvas.DOWN:
                pointer.moveDown( 1 );
                break;

            case Canvas.LEFT:
                pointer.moveLeft( 1 );
                break;

            case Canvas.RIGHT:
                pointer.moveRight( 1 );
                break;

            case Canvas.FIRE:
                final DudeGrid dudeGrid = mState.getDudeGrid();
                final int removeCount = dudeGrid.removeDudes();
                if ( removeCount > 0 )
                {
                    final int increment = getScoreIncrement( removeCount );
                    mState.incrementScoreLevel( increment );
                    mState.incrementScoreTotal( increment );
                    mState.incrementRemoveCountLevel();
                    
                    mHOF.addRemove( new Score( removeCount, mState.getLevel() ) );
                    
                    if ( dudeGrid.allDudesRemoved() )
                    {
                        dromeFinished = true;
                        dromeFinishSuccess = true;
                    }
                    else if ( !dudeGrid.isPossibleToRemoveDudes() )
                    {
                        dromeFinished = true;
                        dromeFinishSuccess = false;
                    }

                    // Update remove count string
                    synchronized ( mSync )
                    {
                        // Cancel possibly pending timer
                        if ( mRemoveCountTimerTask != null )
                        {
                            mRemoveCountTimerTask.cancel();
                            mRemoveCountTimerTask = null;
                        }
                        
                        if ( removeCount >= REMOVE_COUNT_SHOW_MIN )
                        {
                            mRemoveCountString = Integer.toString( removeCount );
                            mRemoveCountTimerTask = new TimerTask()
                                {
                                    public final void run()
                                    {
                                        clearRemoveCountString();
                                    }
                                };
                            mRemoveCountTimer.schedule( mRemoveCountTimerTask,
                                                        REMOVE_COUNT_TIMER_DELAY_MS );
                        }
                        else
                        {
                            mRemoveCountString = null;
                        }
                    }
                }
                updateCurrentCell = false;
                break;

            default:
                repaint = false;
                updateCurrentCell = false;
                break;
        }

        if ( updateCurrentCell )
        {
            mState.getDudeGrid()
                  .setCurrentCell( pointer.getCell() );
        }

        if ( repaint )
        {
            repaint();
        }

        if ( dromeFinished )
        {
            dromeFinished( dromeFinishSuccess, false );
        }
    }

    final void dromeFinished( final boolean pSuccess, final boolean pInitialState )
    {
        if ( !mFinished )
        {
            mFinished = true;
            
            if ( pSuccess )
            {
                mFinishedString = Resources.TEXT_FINISHED_SUCCESS;
                mMenu.setNextLevelEnabled( true );
            }
            else
            {
                mFinishedString = Resources.TEXT_FINISHED_FAIL;
            }
            
            if ( !pInitialState )
            {
                if ( pSuccess )
                {
                    updateLevelStats( pSuccess );
                }
                else
                {
                    updateTotalStats();
                }
                repaint();
            }
        }
    }
    
    final void gameExiting()
    {
        MobileStorable.deleteStore( STORE_GAME_STATE );
        if ( !(    mFinished
                && mFinishedString.equals( Resources.TEXT_FINISHED_FAIL ) ) )
        {
            final RecordStore store =
                MobileStorable.openStore( STORE_GAME_STATE, true, false );
            if ( store != null )
            {
                try
                {
                    final byte[] data = mState.getAsBytes();
                    store.addRecord( data, 0, data.length );
                }
                catch ( final RecordStoreException e )
                {
                    MobileMain.error( "Failed to store game state", e );
                }
                catch ( final IOException e )
                {
                    MobileMain.error( "Failed to store game state", e );
                }
                finally
                {
                    MobileStorable.closeStore( store );
                }
            }
        }
    }

    private final int getScoreIncrement( final int pRemoveCount )
    {
        return ( pRemoveCount - 1 ) * 2 - 1;
    }
    
    private final void updateTotalStats()
    {
        mHOF.addScoreTotal( new Score( mState.getScoreTotal(), mState.getLevel() ) );
    }

    private final void updateLevelStats( final boolean pSuccess )
    {
        final int level = mState.getLevel();
        mHOF.addScoreLevel( new Score( mState.getScoreLevel(), level ) );
        if ( pSuccess )
        {
            mHOF.addRemovesLevel( new Score( mState.getRemoveCountLevel(), level ) );
        }
    }
    
    public final void run()
    {
        // TODO Auto-generated method stub
    }

    final void restart()
    {
        reinit();
        mState.setScoreTotal( 0 );
        mState.setLevel( 1 );
        repaint();
    }

    final void startNextLevel()
    {
        mState.incrementLevel();
        reinit();
        repaint();
    }
    
    final void viewHallOfFame()
    {
        mHOF.showHallOfFame();
    }
    
    final Display getDisplay()
    {
        return mDisplay;
    }
    
    final void show()
    {
        mDisplay.setCurrent( this );
    }
}
