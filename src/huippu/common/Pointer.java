package huippu.common;

public abstract class Pointer
    extends DromeComponent
{
    protected static final int LINE_THICKNESS = 2;
    
    public Pointer( final int pColumnCount, final int pRowCount )
    {
        super( pColumnCount, pRowCount );
    }
}
