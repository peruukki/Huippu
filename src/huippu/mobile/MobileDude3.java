package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude3 extends MobileDude
{
    static final int COLOR_DRAW = 0x002020CC;

    public MobileDude3( final int pId )
    {
        super( pId );
    }

    public void draw( final Graphics pGraphics )
    {
        pGraphics.setColor( COLOR_DRAW );
        pGraphics.fillArc( mScreenX + mOffsetX,
                           mScreenY + mOffsetY,
                           mCellWidth - ( 2 * mOffsetX ),
                           mCellHeight - ( 2 * mOffsetY ),
                           0,
                           360 );
    }
}
