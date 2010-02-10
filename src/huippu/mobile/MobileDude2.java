package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude2 extends MobileDude
{
    static final int COLOR_DRAW = 0x0020AA40;

    public MobileDude2( final int pId )
    {
        super( pId );
        mColorDraw = COLOR_DRAW;
    }

    protected void draw( final Graphics pG, final int pOffsetX,
                         final int pOffsetY )
    {
        pG.fillTriangle( mScreenX + ( mCellWidth / 2 ) - 1 + pOffsetX,
                         mScreenY + mOffsetY + pOffsetY,
                         mScreenX + mOffsetX + pOffsetX,
                         mScreenY + mCellHeight - mOffsetY - 2 + pOffsetY,
                         mScreenX + mCellWidth - mOffsetX - 2 + pOffsetX,
                         mScreenY + mCellHeight - mOffsetY - 2 + pOffsetY );
    }
}
