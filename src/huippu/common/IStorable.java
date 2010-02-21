package huippu.common;

import java.io.IOException;

public interface IStorable
{
    public byte[] getAsBytes()
        throws IOException;
}
