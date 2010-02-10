package huippu.mobile;

import javax.microedition.lcdui.Graphics;

final class MobileDude1 extends MobileDude
{
    static final int COLOR_DRAW = 0x00CC2020;
    
    public MobileDude1( final int pId )
    {
        super( pId );
        mColorDraw = COLOR_DRAW;
    }
    
    protected final void draw( final Graphics pG, final int pOffsetX,
                               final int pOffsetY )
    {
        pG.fillRect( mLeftX + pOffsetX,
                     mTopY + pOffsetY,
                     mWidth,
                     mHeight );
    }
}
