package huippu.mobile;

import javax.microedition.lcdui.Command;

interface ConfirmationReceiver
{
    void handleResult( Command pCommand, int pResult );
}
