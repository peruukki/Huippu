package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude2 extends MobileDude
{
    static final int COLOR_DRAW = 0x0020AA40;

    public MobileDude2( final int pId, final int pColumnCount, final int pRowCount )
    {
        super( pId, pColumnCount, pRowCount );
    }

    public void draw( final Graphics pGraphics )
    {
        pGraphics.setColor( COLOR_DRAW );
        pGraphics.fillRect( mScreenX + CELL_OFFSET + 1,
                            mScreenY + CELL_OFFSET,
                            mCellWidth - 2 * CELL_OFFSET - 2,
                            mCellHeight - 2 * CELL_OFFSET );
    }
}
