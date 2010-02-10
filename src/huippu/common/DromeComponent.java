package huippu.common;


public abstract class DromeComponent
{
    protected static int mColumnCount = 0;
    protected static int mRowCount = 0;
    
    protected static int mCellWidth = 0;
    protected static int mCellHeight = 0;
    
    protected Cell mCell;
    
    protected int mScreenX;
    protected int mScreenY;
    
    public static void setGridSize( final int pColumnCount, final int pRowCount )
    {
        mColumnCount = pColumnCount;
        mRowCount = pRowCount;
    }
    
    public static void setCellSize( final int pCellWidth, final int pCellHeight )
    {
        mCellWidth = pCellWidth;
        mCellHeight = pCellHeight;
    }
    
    public final void setCellPosition( final Cell pCell )
    {
        mCell = pCell;
    }
    
    public final Cell getCell()
    {
        return mCell;
    }
    
    public void updateScreenPosition()
    {
        mScreenX = mCell.x * mCellWidth;
        mScreenY = mCell.y * mCellHeight;
    }
    
    public void moveLeft( final int pCellCount )
    {
        mCell.x -= pCellCount;
        if ( mCell.x < 0 )
        {
            mCell.x += mColumnCount;
        }
        mScreenX = mCell.x * mCellWidth;
    }
    
    public void moveRight( final int pCellCount )
    {
        mCell.x += pCellCount;
        if ( mCell.x >= mColumnCount )
        {
            mCell.x -= mColumnCount;
        }
        mScreenX = mCell.x * mCellWidth;
    }
    
    public void moveDown( final int pCellCount )
    {
        mCell.y += pCellCount;
        if ( mCell.y >= mRowCount )
        {
            mCell.y -= mRowCount;
        }
        mScreenY = mCell.y * mCellHeight;
    }
    
    public void moveUp( final int pCellCount )
    {
        mCell.y -= pCellCount;
        if ( mCell.y < 0 )
        {
            mCell.y += mRowCount;
        }
        mScreenY = mCell.y * mCellHeight;
    }
}
