package huippu.mobile;

import huippu.common.Dude;

import javax.microedition.lcdui.Graphics;

abstract class MobileDude extends Dude
{
    public static final int DUDE_COUNT = 3;
    
    private static final int COLOR_REMOVABLE = 0x00BBBBBB;
    private static final int COLOR_CHANGE_BORDER = 0x30;
    
    protected int mColorDraw;
    protected int mColorDrawDarker;
    protected int mColorDrawLighter;
    
    public MobileDude( final int pId, final int pColorDraw )
    {
        super( pId );
        mColorDraw = pColorDraw;
        mColorDrawDarker = getBorderColor( mColorDraw, true );
        mColorDrawLighter = getBorderColor( mColorDraw, false );
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
        
        // Draw Dude "down"
        pG.setColor( mColorDraw );
        draw( pG, 1, 1, true );
    }
    
    public final void draw( final Object pGraphics )
    {
        final Graphics g = (Graphics) pGraphics;
        
        // Draw Dude
        g.setColor( mColorDraw );
        draw( g, 1, 1, false );
    }
    
    static final int getBorderColor( final int pColor, final boolean pDarker )
    {
        int borderColor = pColor;
        final int change = COLOR_CHANGE_BORDER;

        for ( int shift = 16; shift >= 0; shift -= 8 )
        {
            final int mask = 0xFF << shift;
            int component = ( pColor & mask ) >> shift;
            
            if ( pDarker )
            {
                component -= change;
                component = ( component < 0x00 ) ? 0x00 : component;
            }
            else
            {
                component += change;
                component = ( component > 0xFF ) ? 0xFF : component;
            }
            
            borderColor &= ~mask;
            borderColor |= component << shift;
        }

        return borderColor;
    }
    
    protected abstract void draw( final Graphics pG, final int pOffsetX,
                                  final int pOffsetY, final boolean pRemovable );
}
