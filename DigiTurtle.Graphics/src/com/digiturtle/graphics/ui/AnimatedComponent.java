package com.digiturtle.graphics.ui;

import org.joml.Vector2d;

import com.digiturtle.math.Line;
import com.digiturtle.math.MathUtils;
import com.digiturtle.math.MultiLine;
import com.digiturtle.math.Path;

public interface AnimatedComponent {
	
	public void reset();
	
	public void setSpeed(double speed);
	
	public void update(float delta);
	
	public void setLooping(boolean looping);
	
	public void setInitial(double t);
	
	public class AnimatedMovement implements AnimatedComponent {
		
		private double t, time, speed;
		
		private Path path;
		
		private boolean looping;
		
		public AnimatedMovement(Path path, double time) {
			t = 0;
			this.time = time;
			this.path = path;
			speed = 0;
		}
		
		@Override
		public void setInitial(double t) {
			this.t = t;
		}
		
		@Override
		public void reset() {
			t = 0;
		}
		
		@Override
		public void setSpeed(double speed) {
			//Logger.debug("AnimatedMovement.setSpeed(double)", speed);
			this.speed = speed;
		}

		@Override
		public void update(float delta) {
			//Logger.debug("Update()", delta, path.getClass().getSimpleName());
			double normalizedDelta = (delta / time) * speed;
			t = MathUtils.clamp(0, t + normalizedDelta, 1);
			if (looping && t == 1) {
				reset();
			}
		}
		
		@Override
		public void setLooping(boolean looping) {
			this.looping = looping;
		}
		
		public double getAngle() {
			if (path instanceof MultiLine) {
				return ((MultiLine) path).getAngleAtLength(t * path.getLength());
			}
			if (path instanceof Line) {
				return ((Line) path).getAngleAtLength(t * path.getLength());
			}
			return 0;
		}
		
		public Vector2d getPosition() {
			return path.getPointAtNormalizedLength(t).round();
		}
		
	}

}
