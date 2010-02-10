package huippu.mobile;

import huippu.common.Pointer;

import javax.microedition.lcdui.Graphics;

final class MobilePointer extends Pointer
{
    private static final int COLOR_DRAW = 0x00FF7799;

    public void draw( final Graphics pG )
    {
        pG.setColor( COLOR_DRAW );
        drawPointer( pG );
    }

    public void moveLeft( final int pCellCount )
    {
        super.moveLeft( pCellCount );
    }
    
    private void drawPointer( final Graphics pG )
    {
        for ( int i = 0; i < LINE_THICKNESS; i++ )
        {
            final int diff = i - 1;
            pG.drawRect( mScreenX + diff,
                         mScreenY + diff,
                         mCellWidth - 1 - ( 2 * diff ),
                         mCellHeight - 1 - ( 2 * diff ) );
        }
    }
}
