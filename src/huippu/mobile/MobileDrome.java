package huippu.mobile;

import huippu.common.Cell;
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
    private static final int DROME_THICKNESS = 1;
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

    private int mScoreLevel = 0;
    private int mScoreTotal = 0;
    private int mRemoveCountLevel = 0;
    
    private final MobileHallOfFame mHOF = new MobileHallOfFame( this, FONT );

    private int mLevel = 1;
    
    private final Display mDisplay;
    private final MobileMenu mMenu;
    private final MobilePointer mPointer;
    private final MobileDudeGrid mDudeGrid;
    
    private String mFinishedString = null;
    private boolean mFinished = false;
	
    public MobileDrome( final MobileMain pApplication )
    {
        mDisplay = Display.getDisplay( pApplication );
        mMenu = new MobileMenu( pApplication, this );
        mPointer = new MobilePointer( mColumnCount, mRowCount );
        mDudeGrid = new MobileDudeGrid( mColumnCount, mRowCount );
        setFullScreenMode( true );
        reinit();
        sizeChanged( getWidth(), getHeight() );
    }
    
    private final void reinit()
    {
        initPointer();
        initDudes();
        mScoreLevel = 0;
        mRemoveCountLevel = 0;
        mFinishedString = null;
        mFinished = false;
        mMenu.setNextLevelEnabled( false );
    }
    
    protected final void sizeChanged( final int pWidth, final int pHeight )
    {
        initDrome( pWidth - REDUCE_WIDTH, pHeight - REDUCE_HEIGHT );
        mPointer.updateScreenPosition( mCellWidth, mCellHeight );
        mDudeGrid.updateDudePositions( mCellWidth, mCellHeight );
    }
    
    private final void initDrome( final int pWidth, final int pHeight )
    {
        mScreenWidth = pWidth;
        mScreenHeight = pHeight;
        
        mDromeWidth = round( pWidth, mColumnCount );
        mDromeHeight = round( pHeight - 2 * TEXT_HEIGHT, mRowCount );
        
        mDromeOffsetX = ( mScreenWidth - mDromeWidth ) / 2;
        mDromeOffsetY = 0;
        
        mCellWidth = ( mDromeWidth - 2 * DROME_THICKNESS ) / mColumnCount;
        mCellHeight = ( mDromeHeight - 2 * DROME_THICKNESS ) / mRowCount;
    }
    
    private final void initPointer()
    {
        mPointer.setCellPosition( new Cell( 0, 0 ) );
        mPointer.updateScreenPosition( mCellWidth, mCellHeight );
    }
        
    private final void initDudes()
    {
        mDudeGrid.fillWithDudes( mCellWidth, mCellHeight, mLevel );
        mDudeGrid.setCurrentCell( mPointer.getCell() );
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
        final int delta = ( pValue - ( 2 * DROME_THICKNESS ) ) % pCellCount;
        return pValue - delta;
    }

    protected void paint( final Graphics g )
    {
        g.setFont( FONT );
        redrawScreen( g );
        g.translate( mDromeOffsetX + DROME_THICKNESS,
                     mDromeOffsetY + DROME_THICKNESS );

        drawDudes( g );
        mPointer.draw( g );
        drawFinishedString( g );

        g.translate( -mDromeOffsetX - DROME_THICKNESS,
                     -mDromeOffsetY - DROME_THICKNESS );
    }

    private final void redrawScreen( final Graphics g )
    {
        // Clear background
        g.setColor( Resources.COLOR_BG_OTHER );
        g.fillRect( 0, 0, mScreenWidth, mScreenHeight );
        
        // Fill drome with background color
        g.setColor( Resources.COLOR_BG_DROME );
        g.fillRect( mDromeOffsetX,
                    mDromeOffsetY,
                    mDromeWidth - 1,
                    mDromeHeight - 1 );

        // Draw borders
        g.setColor( Resources.COLOR_BORDER );
        g.drawRect( mDromeOffsetX,
                    mDromeOffsetY,
                    mDromeWidth - 1,
                    mDromeHeight - 1);

        final int bottomY = mScreenHeight - 2;
        final int leftX = TEXT_OFFSET;
        final int rightX = mScreenWidth - TEXT_OFFSET;
        g.setColor( Resources.COLOR_TEXT );

        // Display level score
        g.drawString( Resources.TEXT_SCORE_LEVEL + mScoreLevel,
                      leftX,
                      bottomY - TEXT_HEIGHT,
                      Graphics.BOTTOM | Graphics.LEFT );

        // Display total score
        g.drawString( Resources.TEXT_SCORE_TOTAL + mScoreTotal,
                      leftX,
                      bottomY,
                      Graphics.BOTTOM | Graphics.LEFT );

        
        // Display screen size
//        g.drawString(   Resources.TEXT_SCREEN_SIZE + mDromeWidth
//                      + " x " + mDromeHeight,
//                      rightX,
//                      bottomY - 2 * TEXT_HEIGHT,
//                      Graphics.BOTTOM | Graphics.RIGHT );
        
        // Display level
        g.drawString( Resources.TEXT_LEVEL + mLevel,
                      rightX,
                      bottomY - TEXT_HEIGHT,
                      Graphics.BOTTOM | Graphics.RIGHT );
        
        // Display remove count
        g.drawString( Resources.TEXT_REMOVES_LEVEL + mRemoveCountLevel,
                      rightX,
                      bottomY,
                      Graphics.BOTTOM | Graphics.RIGHT );
    }
    
    private final void drawFinishedString( final Graphics g )
    {
        if ( mFinishedString != null )
        {
            final int stringWidth = g.getFont()
                                     .stringWidth( mFinishedString );
            final int leftX = ( ( mDromeWidth - stringWidth ) / 2 ) - 2;
            final int topY = ( mDromeHeight / 2) - ( TEXT_HEIGHT / 2 ) - 2;
            
            g.setColor( Resources.COLOR_BORDER );
            g.drawRect( leftX,
                        topY,
                        stringWidth + 3,
                        TEXT_HEIGHT + 1 );
            g.setColor( Resources.COLOR_BG_OTHER );
            g.fillRect( leftX + 1,
                        topY + 1,
                        stringWidth + 2,
                        TEXT_HEIGHT );
            g.setColor( Resources.COLOR_TEXT );
            g.drawString( mFinishedString,
                          leftX + 2,
                          topY + 2,
                          Graphics.TOP | Graphics.LEFT );
        }
    }

    private final void drawDudes( final Graphics pGraphics )
    {
        mDudeGrid.drawDudesAll( pGraphics );
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
                final int removeCount = mDudeGrid.removeDudes();
                if ( removeCount > 0 )
                {
                    final int increment = getScoreIncrement( removeCount );
                    mScoreLevel += increment;
                    mScoreTotal += increment;
                    mRemoveCountLevel++;
                    
                    if ( mDudeGrid.allDudesRemoved() )
                    {
                        dromeFinished = true;
                        dromeFinishSuccess = true;
                    }
                    else if ( !mDudeGrid.isPossibleToRemoveDudes() )
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
            mDudeGrid.setCurrentCell( mPointer.getCell() );
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
        mHOF.addScoreTotal( new Score( mScoreTotal,
                                       mLevel,
                                       new ScoreDate( new Date() ) ) );
    }

    private final void updateLevelStats( final boolean pSuccess )
    {
        mHOF.addScoreLevel( new Score( mScoreLevel,
                                       mLevel,
                                       new ScoreDate( new Date() ) ) );
        if ( pSuccess )
        {
            mHOF.addRemovesLevel( new Score( mRemoveCountLevel,
                                             mLevel,
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
        mScoreTotal = 0;
        mLevel = 1;
        repaint();
    }

    final void startNextLevel()
    {
        mLevel++;
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
