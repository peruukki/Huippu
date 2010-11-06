package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude1 extends MobileDude
{
    public MobileDude1( final int pId, final int pColorDraw )
    {
        super( pId, pColorDraw );
    }
    
    protected final void draw( final Graphics pG, final int pOffsetX,
                               final int pOffsetY, final boolean pRemovable )
    {
        pG.fillRect( mLeftX + pOffsetX,
                     mTopY + pOffsetY,
                     mWidth,
                     mHeight );
        
        // Top left corner
        pG.setColor( pRemovable ? mColorDrawDarker : mColorDrawLighter );
        pG.drawLine( mLeftX + pOffsetX,
                     mTopY + pOffsetY,
                     mLeftX + pOffsetX + mWidth - 1,
                     mTopY + pOffsetY );
        pG.drawLine( mLeftX + pOffsetX,
                     mTopY + pOffsetY,
                     mLeftX + pOffsetX,
                     mTopY + pOffsetY + mHeight - 1 );
        
        // Bottom right corner
        pG.setColor( pRemovable ? mColorDrawLighter : mColorDrawDarker );
        pG.drawLine( mLeftX + pOffsetX,
                     mTopY + pOffsetY + mHeight - 1,
                     mLeftX + pOffsetX + mWidth - 1,
                     mTopY + pOffsetY + mHeight - 1 );
        pG.drawLine( mLeftX + pOffsetX + mWidth -1,
                     mTopY + pOffsetY,
                     mLeftX + pOffsetX + mWidth - 1,
                     mTopY + pOffsetY + mHeight - 1 );
    }
}
