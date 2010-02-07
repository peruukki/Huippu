package huippu.mobile;

import huippu.common.Pointer;

import javax.microedition.lcdui.Graphics;

final class MobilePointer extends Pointer
{
    private static final int COLOR_DRAW = 0x00FF8080;

    public MobilePointer( final int pColumnCount, final int pRowCount )
    {
        super( pColumnCount, pRowCount );
    }

    public void draw( final Graphics pGraphics )
    {
        pGraphics.setColor( COLOR_DRAW );
        drawPointer( pGraphics );
    }

    public void moveLeft( final int pCellCount )
    {
        super.moveLeft( pCellCount );
    }
    
    private void drawPointer( final Graphics pGraphics )
    {
        for ( int i = 0; i < LINE_THICKNESS; i++ )
        {
            final int diff = i - 1;
            pGraphics.drawRect( mScreenX + diff,
                                mScreenY + diff,
                                mCellWidth - 1 - ( 2 * diff ),
                                mCellHeight - 1 - ( 2 * diff ) );
        }
    }
}
