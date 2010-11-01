package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude3 extends MobileDude
{
    static final int COLOR_DRAW = 0x002020CC;

    public MobileDude3( final int pId )
    {
        super( pId, COLOR_DRAW );
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
