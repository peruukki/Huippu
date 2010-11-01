package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude2 extends MobileDude
{
    static final int COLOR_DRAW = 0x0020AA40;
    
    private int mCenterX = 0;
    private int mUpperTriangleBottomY = 0;
    private int mLowerTriangleTopY = 0;

    public MobileDude2( final int pId )
    {
        super( pId, COLOR_DRAW );
    }

    public final void updateScreenPosition()
    {
        super.updateScreenPosition();
        updatePositionX();
        updatePositionY();
    }
    
    public final void moveLeft( final int pCellCount )
    {
        super.moveLeft( pCellCount );
        updatePositionX();
    }
    
    public final void moveRight( final int pCellCount )
    {
        super.moveRight( pCellCount );
        updatePositionX();
    }
    
    public final void moveDown( final int pCellCount )
    {
        super.moveDown( pCellCount );
        updatePositionY();
    }
    
    public final void moveUp( final int pCellCount )
    {
        super.moveUp( pCellCount );
        updatePositionY();
    }
    
    protected final void updatePositionX()
    {
        super.updatePositionX();
        mCenterX = mLeftX + ( mWidth / 2 );
    }
    
    protected final void updatePositionY()
    {
        super.updatePositionY();
        mUpperTriangleBottomY = mTopY + ( mHeight / 2 );
        mLowerTriangleTopY =   mUpperTriangleBottomY
                             + ( 1 - mHeight % 2 );
    }

    protected final void draw( final Graphics pG, final int pOffsetX,
                               final int pOffsetY, final boolean pRemovable )
    {
        // Upper triangle
        pG.fillTriangle( mCenterX + pOffsetX,
                         mTopY + pOffsetY,
                         mLeftX + pOffsetX,
                         mUpperTriangleBottomY + pOffsetY,
                         mRightX + pOffsetX,
                         mUpperTriangleBottomY + pOffsetY );
        
        // Lower triangle
        pG.fillTriangle( mCenterX + pOffsetX,
                         mBottomY - 1 + pOffsetY,
                         mLeftX + pOffsetX,
                         mLowerTriangleTopY + pOffsetY,
                         mRightX + pOffsetX,
                         mLowerTriangleTopY + pOffsetY );
        
        // Top left corner
        pG.setColor( pRemovable ? mColorDrawDarker : mColorDrawLighter );
        pG.drawLine( mCenterX + pOffsetX,
                     mTopY + pOffsetY,
                     mLeftX + pOffsetX,
                     mUpperTriangleBottomY + pOffsetY );
        
        // Top right corner
        pG.drawLine( mCenterX + pOffsetX,
                     mTopY + pOffsetY,
                     mRightX + pOffsetX,
                     mUpperTriangleBottomY + pOffsetY );

        // Bottom left corner
        pG.drawLine( mCenterX + pOffsetX,
                     mBottomY - 1 + pOffsetY,
                     mLeftX + pOffsetX,
                     mLowerTriangleTopY + 1 + pOffsetY );
        
        // Bottom right corner
        pG.setColor( pRemovable ? mColorDrawLighter : mColorDrawDarker );
        pG.drawLine( mCenterX + pOffsetX,
                     mBottomY - 1 + pOffsetY,
                     mRightX + pOffsetX,
                     mLowerTriangleTopY + 1 + pOffsetY );
    }
}
