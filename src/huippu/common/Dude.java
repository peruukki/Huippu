package huippu.common;

public abstract class Dude
    extends DromeComponent
{
    public static final int INVALID_ID = -1;
    
    private static final int DEFAULT_OFFSET = 1;
    protected static int mOffsetX = DEFAULT_OFFSET;
    protected static int mOffsetY = DEFAULT_OFFSET;
    
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
    
    public final int getId()
    {
        return mId;
    }
    
    public boolean equals( final Object pOther )
    {
        return    pOther != null
               && getClass().equals( pOther.getClass() );
    }
}
