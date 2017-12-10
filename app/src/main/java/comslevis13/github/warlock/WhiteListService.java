package comslevis13.github.warlock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by slevi on 12/9/2017.
 */

public class WhiteListService extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
