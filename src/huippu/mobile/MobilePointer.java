package huippu.mobile;

import huippu.common.Cell;
import huippu.common.Pointer;

import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.lcdui.Graphics;

final class MobilePointer
    extends Pointer
{
    private static final int COLOR_DRAW = 0x00FF7799;
    
    public MobilePointer()
    {
        this( new Cell( 0, 0 ) );
    }
    
    public MobilePointer( final DataInputStream pInput )
        throws IOException
    {
        this( new Cell( pInput.readByte(), pInput.readByte() ) );
    }
    
    private MobilePointer( final Cell pCell )
    {
        super();
        setCellPosition( pCell );
    }

    public void draw( final Object pGraphics )
    {
        final Graphics g = (Graphics) pGraphics;
        g.setColor( COLOR_DRAW );
        drawPointer( g );
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
