package huippu.common;

public abstract class Dude
    extends DromeComponent
{
    public static final int INVALID_ID = -1;
    protected static final int CELL_OFFSET = 2;
    
    private final int mId;
    
    public Dude( final int pId, final int pColumnCount, final int pRowCount )
    {
        super( pColumnCount, pRowCount );
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
