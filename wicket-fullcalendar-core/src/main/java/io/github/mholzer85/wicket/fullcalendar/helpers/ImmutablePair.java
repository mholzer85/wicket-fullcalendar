/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
