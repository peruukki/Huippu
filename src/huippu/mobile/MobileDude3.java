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
        pGraphics.fillArc( mScreenX + CELL_OFFSET,
                           mScreenY + CELL_OFFSET,
                           mCellWidth - 1 - CELL_OFFSET,
                           mCellHeight - 1 - CELL_OFFSET,
                           0,
                           360 );
    }
}
