package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude1 extends MobileDude
{
    static final int COLOR_DRAW = 0x00CC2020;
    
    public MobileDude1( final int pId )
    {
        super( pId );
    }

    public final void draw( final Graphics pGraphics )
    {
        pGraphics.setColor( COLOR_DRAW );
        pGraphics.fillRect( mScreenX + mOffsetX,
                            mScreenY + mOffsetY,
                            mCellWidth - 2 * mOffsetX,
                            mCellHeight - 2 * mOffsetY );
    }
}
