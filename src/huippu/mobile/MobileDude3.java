package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude3 extends MobileDude
{
    public MobileDude3( final int pId, final int pColorDraw )
    {
        super( pId, pColorDraw );
    }

    protected final void draw( final Graphics pG, final int pOffsetX,
                               final int pOffsetY, final boolean pRemovable )
    {
        pG.fillArc( mLeftX + pOffsetX,
                    mTopY + pOffsetY,
                    mWidth,
                    mHeight,
                    0,
                    360 );
        
        // Top left half
        pG.setColor( pRemovable ? mColorDrawDarker : mColorDrawLighter );
        pG.drawArc( mLeftX + pOffsetX,
                    mTopY + pOffsetY,
                    mWidth - 1,
                    mHeight - 1,
                    45,
                    180 );
        
        // Bottom right half
        pG.setColor( pRemovable ? mColorDrawLighter : mColorDrawDarker );
        pG.drawArc( mLeftX + pOffsetX,
                    mTopY + pOffsetY,
                    mWidth - 1,
                    mHeight - 1,
                    225,
                    180 );
    }
}
