package huippu.common;

import java.io.DataInputStream;
import java.io.IOException;


public final class GameState
    extends Storable
{
    private final DudeGrid mDudeGrid;

    private int mScoreLevel = 0;
    private int mScoreTotal = 0;
    private int mRemoveCountLevel = 0;
    
    private int mLevel = 1;
    
    public GameState( final DudeGrid pDudeGrid )
    {
        mDudeGrid = pDudeGrid;
    }
    
    public GameState( final DataInputStream pInput )
        throws IOException
    {
        super( pInput );
        // TODO: Set all instance variables from pInput
        mDudeGrid = null;
    }
    
    public byte[] getAsBytes()
        throws IOException
    {
        // TODO: Convert instance variable values to byte data
        return null;
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
