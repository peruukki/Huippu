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

    protected void draw( final Graphics pG, final int pOffsetX,
                         final int pOffsetY )
    {
        pG.fillArc( mScreenX + mOffsetX + pOffsetX,
                    mScreenY + mOffsetY + pOffsetY,
                    mCellWidth - 2 * mOffsetX - 1,
                    mCellHeight - 2 * mOffsetY - 1,
                    0,
                    360 );
    }
}
