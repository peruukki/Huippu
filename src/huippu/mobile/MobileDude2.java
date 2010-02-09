package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude2 extends MobileDude
{
    static final int COLOR_DRAW = 0x0020AA40;

    public MobileDude2( final int pId )
    {
        super( pId );
    }

    public void draw( final Graphics pGraphics )
    {
        pGraphics.setColor( COLOR_DRAW );
        pGraphics.fillTriangle( mScreenX + ( mCellWidth / 2 ),
                                mScreenY + mOffsetY,
                                mScreenX + mOffsetX,
                                mScreenY + mCellHeight - 1 - mOffsetY,
                                mScreenX + mCellWidth - mOffsetX,
                                mScreenY + mCellHeight - 1 - mOffsetY );
    }
}
