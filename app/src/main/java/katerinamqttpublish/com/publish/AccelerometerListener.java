package katerinamqttpublish.com.publish;

public interface AccelerometerListener {

        public void onAccelerationChanged(float x, float y, float z);

        public void onShake(float force);

}
