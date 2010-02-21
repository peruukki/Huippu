package huippu.mobile;

import huippu.common.Dude;

import javax.microedition.lcdui.Graphics;

abstract class MobileDude extends Dude
{
    public static final int DUDE_COUNT = 3;
    
    static final int COLOR_REMOVABLE = 0x00BBBBBB;
    static final int COLOR_SHADOW = 0x00000000;
    
    protected int mColorDraw;
    
    public MobileDude( final int pId )
    {
        super( pId );
    }

    public static final MobileDude getDude( final int pId )
    {
        final MobileDude dude;
        
        switch ( pId )
        {
            case 0:
                dude = new MobileDude1( pId );
                break;
                
            case 1:
                dude = new MobileDude2( pId );
                break;

            case 2:
                dude = new MobileDude3( pId );
                break;
                
            default:
                throw new IllegalArgumentException( "Unknown Dude id " + pId );
        }
        
        return dude;
    }
    
    void drawAsRemovable( final Graphics pG )
    {
        pG.setColor( COLOR_REMOVABLE );
        pG.fillRect( mScreenX,
                     mScreenY,
                     mCellWidth,
                     mCellHeight );
        
        // Draw Dude "down", without shadow
        pG.setColor( mColorDraw );
        draw( pG, 1, 1 );
    }
    
    public final void draw( final Object pGraphics )
    {
        final Graphics g = (Graphics) pGraphics;
        
        // Draw shadow
        g.setColor( COLOR_SHADOW );
        draw( g, 1, 1 );
        
        // Draw Dude
        g.setColor( mColorDraw );
        draw( g, 0, 0 );
    }
    
    protected abstract void draw( final Graphics pG, final int pOffsetX,
                                  final int pOffsetY );
}
