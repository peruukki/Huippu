package huippu.mobile;

import huippu.common.Cell;
import huippu.common.DudeGrid;

import javax.microedition.lcdui.Graphics;

final class MobileDudeGrid extends DudeGrid
{
    public MobileDudeGrid( final int pColumnCount, final int pRowCount )
    {
        super( pColumnCount, pRowCount, MobileDude.DUDE_COUNT );
    }

    public final void fillWithDudes( final int pLevel )
    {
        super.calculateDudeIdProbabilities( pLevel );
        
        for ( int x = 0; x < mColumnCount; x++ )
        {
            for ( int y = 0; y < mRowCount; y++ )
            {
                final MobileDude newDude =
                    MobileDude.getDude( getNextDudeId( x, y ) );
                newDude.setCellPosition( new Cell( x, y ) );
                newDude.updateScreenPosition();
                mDudes[ x ][ y ] = newDude;
            }
        }
    }
    
    public final void drawDudesAll( final Object pGraphics )
    {
        final Graphics g = (Graphics) pGraphics;
        for ( int x = 0; x < mColumnCount; x++ )
        {
            for ( int y = 0; y < mRowCount; y++ )
            {
                final MobileDude dude = (MobileDude) mDudes[ x ][ y ];
                if ( dude != null )
                {
                    if ( mRemovable[ x ][ y ] )
                    {
                        dude.drawAsRemovable( g );                    
                    }
                    else
                    {
                        dude.draw( g );
                    }
                }
            }
        }
    }
}
