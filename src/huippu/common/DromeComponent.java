package huippu.common;


public abstract class DromeComponent
{
    public static final byte DOWN = 2;
    public static final byte LEFT = 4;
    public static final byte RIGHT = 6;
    public static final byte UP = 8;
    
    protected final int mColumnCount;
    protected final int mRowCount;
    
    protected int mCellWidth;
    protected int mCellHeight;
    
    protected Cell mCell;
    
    protected int mScreenX;
    protected int mScreenY;
    
    public DromeComponent( final int pColumnCount, final int pRowCount )
    {
        mColumnCount = pColumnCount;
        mRowCount = pRowCount;
    }
    
    public final void setCellPosition( final Cell pCell )
    {
        mCell = pCell;
    }
    
    public final Cell getCell()
    {
        return mCell;
    }
    
    public final void updateScreenPosition( final int pCellWidth,
                                            final int pCellHeight )
    {
        mCellWidth = pCellWidth;
        mCellHeight = pCellHeight;
        
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
