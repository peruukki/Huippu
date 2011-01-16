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
    
    protected int mCurrentScreenX = 0;
    protected int mCurrentScreenY = 0;
    protected int mCurrentOffsetX = 0;
    protected int mCurrentOffsetY = 0;
    protected static final int mMoveChangeX = 4;
    protected static final int mMoveChangeY = mMoveChangeX;
    protected boolean mIsMoving = false;

    protected static final int mGoOverOffsetX = 0;
    protected static final int mGoOverOffsetY = 2;
    protected static final int mGoOverWaitRoundsX = 3;
    protected static final int mGoOverWaitRoundsY = mGoOverWaitRoundsX;
    protected static final int mGoUnderWaitRoundsX = 2;
    protected static final int mGoUnderWaitRoundsY = mGoUnderWaitRoundsX;
    protected int mGoOverWaitCountX = 0;
    protected int mGoOverWaitCountY = 0;
    protected boolean mGoOverX = false;
    protected boolean mGoUnderX = false;
    protected boolean mGoOverY = false;
    protected boolean mGoUnderY = false;
    
    protected boolean mIsRemoved = false;
    
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
    
    private static final int updateCurrentOffset( final int pOffset,
                                                  final int pChange,
                                                  final int pGoOverOffset )
    {
        int newOffset = pOffset;
        
        if ( pOffset < 0 )
        {
            newOffset += pChange;
            if ( newOffset > 0 )
            {
                newOffset = 0;
            }
        }
        else if ( pOffset > 0 )
        {
            newOffset -= pChange;
            if ( newOffset < 0 )
            {
                newOffset = 0;
            }
        }
        
        if (    newOffset == 0
             && pGoOverOffset != 0 )
        {
            newOffset = pGoOverOffset;
        }
        
        return newOffset;
    }
    
    public final void setRemoved()
    {
        mIsRemoved = true;
    }
    
    public final boolean isRemoved()
    {
        return mIsRemoved;
    }
    
    public final boolean move()
    {
        if ( mIsMoving )
        {
            if ( mGoOverWaitCountX == 0 )
            {
                mCurrentOffsetX =
                    updateCurrentOffset( mCurrentOffsetX,
                                         mMoveChangeX,
                                            mGoOverX
                                         ? -mGoOverOffsetX
                                         : 0 );
                if ( mCurrentOffsetX < 0 )
                {
                    mGoOverX = false;
                    mGoOverWaitCountX = mGoOverWaitRoundsX;
                }
            }
            else
            {
                --mGoOverWaitCountX;
            }
            
            if ( mGoOverWaitCountY == 0 )
            {
                mCurrentOffsetY =
                    updateCurrentOffset( mCurrentOffsetY,
                                         mMoveChangeY,
                                         (   mGoOverY
                                           ? mGoOverOffsetY
                                           : (   mGoUnderY
                                               ? -mGoOverOffsetY
                                               : 0 ) ) );
                if ( mCurrentOffsetY > 0 )
                {
                    mGoOverY = false;
                    mGoUnderY = true;
                    mGoOverWaitCountY = mGoOverWaitRoundsY;
                }
                else if ( mGoUnderY )
                {
                    mGoUnderY = false;
                    mGoOverWaitCountY = mGoUnderWaitRoundsY;
                }
            }
            else
            {
                --mGoOverWaitCountY;
            }
            
            updateScreenPosition();
            mIsMoving =    ( mCurrentOffsetX != 0 )
                        || ( mCurrentOffsetY != 0 );
        }
        
        return mIsMoving;
    }
    
    public void moveLeft( final int pCellCount )
    {
        final int oldX = mScreenX;
        super.moveLeft( pCellCount );
        mCurrentOffsetX += oldX - mScreenX;
        mGoOverX = true;
        mIsMoving = true;
        updatePositionX();
    }
    
    public void moveRight( final int pCellCount )
    {
        super.moveRight( pCellCount );
        updatePositionX();
    }
    
    public void moveDown( final int pCellCount )
    {
        final int oldY = mScreenY;
        super.moveDown( pCellCount );
        mCurrentOffsetY += oldY - mScreenY;
        mGoOverY = true;
        mIsMoving = true;
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
    
    public final boolean isMoving()
    {
        return mIsMoving;
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
        if ( mIsMoving )
        {
            mLeftX += mCurrentOffsetX;
            mRightX += mCurrentOffsetX;
        }
    }
    
    protected void updatePositionY()
    {
        mTopY = mScreenY + mOffsetY;
        mBottomY = mScreenY + mCellHeight - mOffsetY - 2;
        mHeight = mBottomY - mTopY;
        if ( mIsMoving )
        {
            mTopY += mCurrentOffsetY;
            mBottomY += mCurrentOffsetY;
        }
    }
}
