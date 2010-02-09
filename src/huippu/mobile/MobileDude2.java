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
        pGraphics.fillTriangle( mScreenX + ( mCellWidth / 2 ),
                                mScreenY + CELL_OFFSET,
                                mScreenX + CELL_OFFSET,
                                mScreenY + mCellHeight - CELL_OFFSET,
                                mScreenX + mCellWidth - CELL_OFFSET,
                                mScreenY + mCellHeight - CELL_OFFSET );
    }
}
