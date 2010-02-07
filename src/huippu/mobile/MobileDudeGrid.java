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

    public final void fillWithDudes( final int pCellWidth, final int pCellHeight,
                                     final int pLevel )
    {
        super.calculateDudeIdProbabilities( pLevel );
        
        for ( int x = 0; x < mColumnCount; x++ )
        {
            for ( int y = 0; y < mRowCount; y++ )
            {
                final MobileDude newDude =
                    MobileDude.getDude( mColumnCount,
                                        mRowCount,
                                        getNextDudeId( x, y ) );
                newDude.setCellPosition( new Cell( x, y ) );
                newDude.updateScreenPosition( pCellWidth, pCellHeight );
                mDudes[ x ][ y ] = newDude;
            }
        }
    }
    
    final void drawDudesAll( final Graphics pGraphics )
    {
        for ( int x = 0; x < mColumnCount; x++ )
        {
            for ( int y = 0; y < mRowCount; y++ )
            {
                final MobileDude dude = (MobileDude) mDudes[ x ][ y ];
                if ( dude != null )
                {
                    if ( mRemovable[ x ][ y ] )
                    {
                        dude.drawAsRemovable( pGraphics );                    
                    }
                    else
                    {
                        dude.draw( pGraphics );
                    }
                }
            }
        }
    }
}
