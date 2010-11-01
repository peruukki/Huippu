package huippu.common;

public abstract class Dude
    extends DromeComponent
{
    public static final int INVALID_ID = -1;
    
    private static final int DEFAULT_OFFSET = 0;
    protected static int mOffsetX = DEFAULT_OFFSET;
    protected static int mOffsetY = DEFAULT_OFFSET;
    
    protected int mLeftX = 0;
    protected int mRightX = 0;
    protected int mTopY = 0;
    protected int mBottomY = 0;
    protected int mWidth = 0;
    protected int mHeight = 0;
    
    private final int mId;
    
    public static void updateSize()
    {
        int difference;
        if ( mCellWidth < mCellHeight )
        {
            difference = mCellHeight - mCellWidth;
        }
        else
        {
            difference = mCellWidth - mCellHeight;
        }
        if ( difference > 1 )
        {
            difference = 1;
        }
        
        if ( mCellWidth < mCellHeight )
        {
            mOffsetX = DEFAULT_OFFSET;
            mOffsetY = DEFAULT_OFFSET + difference;
        }
        else
        {
            mOffsetX = DEFAULT_OFFSET + difference;
            mOffsetY = DEFAULT_OFFSET;
        }
    }
    
    public Dude( final int pId )
    {
        super();
        mId = pId;
    }
    
    public void updateScreenPosition()
    {
        super.updateScreenPosition();
        updatePositionX();
        updatePositionY();
    }
    
    public void moveLeft( final int pCellCount )
    {
        super.moveLeft( pCellCount );
        updatePositionX();
    }
    
    public void moveRight( final int pCellCount )
    {
        super.moveRight( pCellCount );
        updatePositionX();
    }
    
    public void moveDown( final int pCellCount )
    {
        super.moveDown( pCellCount );
        updatePositionY();
    }
    
    public void moveUp( final int pCellCount )
    {
        super.moveUp( pCellCount );
        updatePositionY();
    }
    
    public final int getId()
    {
        return mId;
    }
    
    public boolean equals( final Object pOther )
    {
        return    pOther != null
               && getClass().equals( pOther.getClass() );
    }
    
    protected void updatePositionX()
    {
        mLeftX = mScreenX + mOffsetX;
        mRightX = mScreenX + mCellWidth - mOffsetX - 2;
        mWidth = mRightX - mLeftX;
    }
    
    protected void updatePositionY()
    {
        mTopY = mScreenY + mOffsetY;
        mBottomY = mScreenY + mCellHeight - mOffsetY - 2;
        mHeight = mBottomY - mTopY;
    }
}
