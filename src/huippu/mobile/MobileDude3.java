package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude3 extends MobileDude
{
    static final int COLOR_DRAW = 0x002020CC;

    public MobileDude3( final int pId )
    {
        super( pId );
        mColorDraw = COLOR_DRAW;
    }

    protected final void draw( final Graphics pG, final int pOffsetX,
                               final int pOffsetY )
    {
        pG.fillArc( mLeftX + pOffsetX,
                    mTopY + pOffsetY,
                    mWidth,
                    mHeight,
                    0,
                    360 );
    }
}
