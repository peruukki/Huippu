package huippu.common;

import java.io.DataInputStream;
import java.io.IOException;


public final class GameState
    extends Storable
{
    private final DudeGrid mDudeGrid;
    private boolean mInitFromStore;

    private int mScoreLevel = 0;
    private int mScoreTotal = 0;
    private int mRemoveCountLevel = 0;
    
    private int mLevel = 1;
    
    public GameState( final DudeGrid pDudeGrid )
    {
        mDudeGrid = pDudeGrid;
        mInitFromStore = false;
    }
    
    public GameState( final DudeGrid pDudeGrid, final DataInputStream pInput )
        throws IOException
    {
        mDudeGrid = pDudeGrid; 
        
        mScoreTotal = pInput.readInt();
        mScoreLevel = pInput.readInt();
        mRemoveCountLevel = pInput.readInt();
        mLevel = pInput.readInt();
        
        mInitFromStore = true;
    }
    
    public byte[] getAsBytes()
        throws IOException
    {
        // Convert all values to byte data
        final byte[] dataDudeGrid = mDudeGrid.getAsBytes();
        
        final byte[] dataScoreTotal = getDataInt( mScoreTotal );
        final byte[] dataScoreLevel = getDataInt( mScoreLevel );
        final byte[] dataRemoveCountLevel = getDataInt( mRemoveCountLevel );
        final byte[] dataLevel = getDataInt( mLevel );
        
        // Append all data to a continuous byte array
        final byte[] data = new byte[   dataDudeGrid.length
                                      + dataScoreTotal.length
                                      + dataScoreLevel.length
                                      + dataRemoveCountLevel.length
                                      + dataLevel.length ];
        
        int offset = 0;
        
        offset = appendData( dataDudeGrid, data, offset );
        
        offset = appendData( dataScoreTotal, data, offset );
        offset = appendData( dataScoreLevel, data, offset );
        offset = appendData( dataRemoveCountLevel, data, offset );
        offset = appendData( dataLevel, data, offset );
        
        return data;
    }
    
    public final boolean isRestored()
    {
        return mInitFromStore;
    }
    
    public final void clearRestored()
    {
        mInitFromStore = false;
    }
    
    public final DudeGrid getDudeGrid()
    {
        return mDudeGrid;
    }

    public final int getScoreLevel()
    {
        return mScoreLevel;
    }

    public final void setScoreLevel( final int pScoreLevel )
    {
        mScoreLevel = pScoreLevel;
    }
    
    public final void incrementScoreLevel( final int pIncrement )
    {
        mScoreLevel += pIncrement;
    }

    public final int getScoreTotal()
    {
        return mScoreTotal;
    }

    public final void setScoreTotal( final int pScoreTotal )
    {
        mScoreTotal = pScoreTotal;
    }
    
    public final void incrementScoreTotal( final int pIncrement )
    {
        mScoreTotal += pIncrement;
    }

    public final int getRemoveCountLevel()
    {
        return mRemoveCountLevel;
    }

    public final void setRemoveCountLevel( final int pRemoveCountLevel )
    {
        mRemoveCountLevel = pRemoveCountLevel;
    }
    
    public final void incrementRemoveCountLevel()
    {
        mRemoveCountLevel++;
    }

    public final int getLevel()
    {
        return mLevel;
    }

    public final void setLevel( final int pLevel )
    {
        mLevel = pLevel;
    }
    
    public final void incrementLevel()
    {
        mLevel++;
    }
}
