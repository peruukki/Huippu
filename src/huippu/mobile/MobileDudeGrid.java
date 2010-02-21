package huippu.mobile;

import java.io.DataInputStream;
import java.io.IOException;

import huippu.common.Cell;
import huippu.common.Dude;
import huippu.common.DudeGrid;

import javax.microedition.lcdui.Graphics;

final class MobileDudeGrid extends DudeGrid
{
    public MobileDudeGrid( final int pColumnCount, final int pRowCount )
    {
        super( pColumnCount, pRowCount, MobileDude.DUDE_COUNT );
    }
    
    public MobileDudeGrid( final DataInputStream pInput )
        throws IOException
    {
        super( MobileDude.DUDE_COUNT, pInput );
        mIsGridEmpty = true;
        for ( int x = 0; x < mColumnCount; x++ )
        {
            for ( int y = 0; y < mRowCount; y++ )
            {
                final byte id = pInput.readByte();
                if ( id != Dude.INVALID_ID )
                {
                    mIsGridEmpty = false;
                    putDude( id, x, y );
                }
            }
        }
    }

    public final void fillWithDudes( final int pLevel )
    {
        super.calculateDudeIdProbabilities( pLevel );
        
        for ( int x = 0; x < mColumnCount; x++ )
        {
            for ( int y = 0; y < mRowCount; y++ )
            {
                putDude( getNextDudeId( x, y ), x, y );
            }
        }
    }
    
    private final void putDude( final int pDudeId, final int pX, final int pY )
    {
        final MobileDude newDude = MobileDude.getDude( pDudeId );
        newDude.setCellPosition( new Cell( pX, pY ) );
        mDudes[ pX ][ pY ] = newDude;
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
