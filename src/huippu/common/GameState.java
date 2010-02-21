package huippu.common;

import java.io.DataInputStream;
import java.io.IOException;


public final class GameState
    implements IStorable
{
    private final DudeGrid mDudeGrid;

    private int mScoreLevel = 0;
    private int mScoreTotal = 0;
    private int mRemoveCountLevel = 0;
    
    private int mLevel = 1;
    
    private boolean mInitFromStore;
    
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
        
        final byte[] dataScoreTotal = Storable.getDataInt( mScoreTotal );
        final byte[] dataScoreLevel = Storable.getDataInt( mScoreLevel );
        final byte[] dataRemoveCountLevel = Storable.getDataInt( mRemoveCountLevel );
        final byte[] dataLevel = Storable.getDataInt( mLevel );
        
        // Append all data to a continuous byte array
        final byte[] data = new byte[   dataDudeGrid.length
                                      + dataScoreTotal.length
                                      + dataScoreLevel.length
                                      + dataRemoveCountLevel.length
                                      + dataLevel.length ];
        
        int offset = 0;
        
        offset = Storable.appendData( dataDudeGrid, data, offset );
        
        offset = Storable.appendData( dataScoreTotal, data, offset );
        offset = Storable.appendData( dataScoreLevel, data, offset );
        offset = Storable.appendData( dataRemoveCountLevel, data, offset );
        offset = Storable.appendData( dataLevel, data, offset );
        
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
