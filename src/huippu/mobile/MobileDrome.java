package huippu.mobile;

import huippu.common.Cell;
import huippu.common.DromeComponent;
import huippu.common.Dude;
import huippu.common.DudeGrid;
import huippu.common.GameState;
import huippu.common.Resources;
import huippu.common.Score;
import huippu.common.ScoreDate;

import java.util.Date;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

final class MobileDrome
	extends Canvas
	implements Runnable
{
    private static final int TEXT_OFFSET = 2;
    
    private static final Font FONT =
        Font.getFont( Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL );

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
    private final MobilePointer mPointer;
    private final GameState mState;
    
    private String mFinishedString = null;
    private boolean mFinished = false;
	
    public MobileDrome( final MobileMain pApplication )
    {
        mDisplay = Display.getDisplay( pApplication );
        mMenu = new MobileMenu( pApplication, this );
        DromeComponent.setGridSize( mColumnCount, mRowCount );
        mPointer = new MobilePointer();
        mState = new GameState( new MobileDudeGrid( mColumnCount, mRowCount ) );
        setFullScreenMode( true );
        reinit();
        sizeChanged( getWidth(), getHeight() );
    }
    
    private final void reinit()
    {
        initPointer();
        initDudes();
        mState.setScoreLevel( 0 );
        mState.setRemoveCountLevel( 0 );
        mFinishedString = null;
        mFinished = false;
        mMenu.setNextLevelEnabled( false );
    }
    
    protected final void sizeChanged( final int pWidth, final int pHeight )
    {
        initDrome( pWidth - REDUCE_WIDTH, pHeight - REDUCE_HEIGHT );
        DromeComponent.setCellSize( mCellWidth, mCellHeight );
        Dude.updateSize();
        mPointer.updateScreenPosition();
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
        mPointer.setCellPosition( new Cell( 0, 0 ) );
        mPointer.updateScreenPosition();
    }
        
    private final void initDudes()
    {
        final DudeGrid dudeGrid = mState.getDudeGrid(); 
        dudeGrid.fillWithDudes( mState.getLevel() );
        dudeGrid.setCurrentCell( mPointer.getCell() );
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
        pG.setFont( FONT );
        drawScreen( pG );
        pG.translate( mDromeOffsetX, mDromeOffsetY );

        drawDudes( pG );
        mPointer.draw( pG );
        drawFinishedString( pG );

        pG.translate( -mDromeOffsetX, -mDromeOffsetY );
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
        drawString( pG,
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
        drawString( pG,
                    Resources.TEXT_REMOVES_LEVEL + mState.getRemoveCountLevel(),
                    rightX,
                    bottomY,
                    Graphics.BOTTOM | Graphics.RIGHT );
    }
    
    private final void drawFinishedString( final Graphics pG )
    {
        if ( mFinishedString != null )
        {
            final int stringWidth = pG.getFont()
                                      .stringWidth( mFinishedString );
            final int border = 5;
            final int bottomReduction = 2;
            final int leftX = ( ( mDromeWidth - stringWidth ) / 2 ) - border;
            final int topY = ( mDromeHeight / 2) - ( TEXT_HEIGHT / 2 ) - border;
        
            // Draw shadow
            pG.setColor( Resources.COLOR_SHADOW );
            pG.drawRect( leftX + 1,
                         topY + 1,
                         stringWidth + ( 2 * border ) - 2,
                         TEXT_HEIGHT + ( 2 * border ) - 2 - bottomReduction );
            
            // Draw background box
            pG.setColor( Resources.COLOR_BG_OTHER );
            pG.fillRect( leftX,
                         topY,
                         stringWidth + ( 2 * border ) - 1,
                         TEXT_HEIGHT + ( 2 * border ) - 1 - bottomReduction );
            
            // Draw message
            drawString( pG,
                        mFinishedString,
                        leftX + border,
                        topY + border,
                        Graphics.TOP | Graphics.LEFT );
        }
    }
    
    private static final void drawString( final Graphics pG, final String pString,
                                          final int pX, final int pY,
                                          final int pAnchor )
    {
        pG.setColor( Resources.COLOR_SHADOW );
        pG.drawString( pString, pX + 1, pY + 1, pAnchor );
        
        pG.setColor( Resources.COLOR_TEXT );
        pG.drawString( pString, pX, pY, pAnchor );
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
        switch ( gameAction )
        {
            case Canvas.UP:
                mPointer.moveUp( 1 );
                break;

            case Canvas.DOWN:
                mPointer.moveDown( 1 );
                break;

            case Canvas.LEFT:
                mPointer.moveLeft( 1 );
                break;

            case Canvas.RIGHT:
                mPointer.moveRight( 1 );
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
                  .setCurrentCell( mPointer.getCell() );
        }

        if ( repaint )
        {
            repaint();
        }

        if ( dromeFinished )
        {
            dromeFinished( dromeFinishSuccess );
        }
    }

    final void dromeFinished( final boolean pSuccess )
    {
        if ( !mFinished )
        {
            mFinished = true;
            updateLevelStats( pSuccess );
            
            if ( pSuccess )
            {
                mFinishedString = Resources.TEXT_FINISHED_SUCCESS;
                mMenu.setNextLevelEnabled( true );
            }
            else
            {
                mFinishedString = Resources.TEXT_FINISHED_FAIL;
                updateTotalStats();
            }
            
            repaint();
        }
    }

    private final int getScoreIncrement( final int pRemoveCount )
    {
        return ( pRemoveCount - 1 ) * 2 - 1;
    }
    
    private final void updateTotalStats()
    {
        mHOF.addScoreTotal( new Score( mState.getScoreTotal(),
                                       mState.getLevel(),
                                       new ScoreDate( new Date() ) ) );
    }

    private final void updateLevelStats( final boolean pSuccess )
    {
        mHOF.addScoreLevel( new Score( mState.getScoreLevel(),
                                       mState.getLevel(),
                                       new ScoreDate( new Date() ) ) );
        if ( pSuccess )
        {
            mHOF.addRemovesLevel( new Score( mState.getRemoveCountLevel(),
                                             mState.getLevel(),
                                             new ScoreDate( new Date() ) ) );
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
