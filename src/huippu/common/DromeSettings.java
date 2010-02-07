package huippu.common;

public abstract class DromeSettings
{
    private static int mCellWidth;
    private static int mCellHeight;
    
    public synchronized static final int getCellWidth()
    {
        return mCellWidth;
    }
    
    public synchronized static final void setCellWidth( final int pWidth )
    {
        mCellWidth = pWidth;
    }
    
    public synchronized static final int getCellHeight()
    {
        return mCellHeight;
    }
    
    public synchronized static final void setCellHeight( final int pHeight )
    {
        mCellHeight = pHeight;
    }
}
