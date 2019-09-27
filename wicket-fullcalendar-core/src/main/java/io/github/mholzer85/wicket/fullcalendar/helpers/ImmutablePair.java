package io.github.mholzer85.wicket.fullcalendar.helpers;

import java.io.Serializable;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;

public final class ImmutablePair<L, R> implements Serializable {

	private static final long serialVersionUID = 4954918890077093841L;

	public final L left;
	public final R right;


	public ImmutablePair(@Nullable L left, @Nullable R right) {
		this.left = left;
		this.right = right;
	}


	@Nullable
	public L getLeft() {
		return this.left;
	}


	@Nullable
	public R getRight() {
		return this.right;
	}


	@NonNull
	public static <L, R> ImmutablePair<L, R> of(@Nullable L left, @Nullable R right) {
		return new ImmutablePair(left, right);
	}

}
