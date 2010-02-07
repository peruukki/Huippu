package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude1 extends MobileDude
{
    static final int COLOR_DRAW = 0x00CC2020;
    
    public MobileDude1( final int pId, final int pColumnCount, final int pRowCount )
    {
        super( pId, pColumnCount, pRowCount );
    }

    public final void draw( final Graphics pGraphics )
    {
        pGraphics.setColor( COLOR_DRAW );
        pGraphics.fillRect( mScreenX + CELL_OFFSET,
                            mScreenY + CELL_OFFSET + 1,
                            mCellWidth - 2 * CELL_OFFSET,
                            mCellHeight - 2 * CELL_OFFSET - 2 );
    }
}
