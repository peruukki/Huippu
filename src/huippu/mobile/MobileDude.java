package huippu.mobile;

import huippu.common.Dude;

import javax.microedition.lcdui.Graphics;

abstract class MobileDude extends Dude
{
    public static final int DUDE_COUNT = 3;
    
    static final int COLOR_REMOVABLE = 0x00BBBBBB;
    
    public MobileDude( final int pId, final int pColumnCount, final int pRowCount )
    {
        super( pId, pColumnCount, pRowCount );
    }

    public static final MobileDude getDude( final int pColumnCount,
                                            final int pRowCount,
                                            final int pId )
    {
        final MobileDude dude;
        
        switch ( pId )
        {
            case 0:
                dude = new MobileDude1( pId, pColumnCount, pRowCount );
                break;
                
            case 1:
                dude = new MobileDude2( pId, pColumnCount, pRowCount );
                break;

            case 2:
                dude = new MobileDude3( pId, pColumnCount, pRowCount );
                break;
                
            default:
                throw new IllegalArgumentException( "Unknown Dude id " + pId );
        }
        
        return dude;
    }
    
    final void drawAsRemovable( final Graphics pGraphics )
    {
        pGraphics.setColor( COLOR_REMOVABLE );
        pGraphics.fillRect( mScreenX,
                            mScreenY,
                            mCellWidth,
                            mCellHeight );
        draw( pGraphics );
    }
    
    public abstract void draw( final Graphics pGraphics );
}
