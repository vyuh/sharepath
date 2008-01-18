/* 
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yexing.android.sharepath.domain;

import android.net.ContentURI;
import android.provider.BaseColumns;

/**
 * Convenience definitions for SharePathProvider
 */
public final class SharePath {
	/**
	 * Message table
	 */
	public static final class Message implements BaseColumns {
		/**
		 * The content:// style URL for this table
		 */
		public static final ContentURI CONTENT_URI = ContentURI
				.create("content://org.yexing.android.sharepath.domain.SharePath/message");

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "_read ASC,_date DESC";

		/**
		 * The type of the message, 0:request 1:reply
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String TYPE = "_type";

		/**
		 * send from
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String FROM = "_from";

		/**
		 * send to
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String TO = "_to";

		/**
		 * The timestamp for when the message was sent
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String DATE = "_date";

		/**
		 * The read flag 0:no 1:yes
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String READ = "_read";
		
		/**
		 * The level of the map
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String LEVEL = "_level";
		
		/**
		 * The center of the map
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String CENTER = "_center";

		/**
		 * The path
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String PATH = "_path";

	}

	/**
	 * Buddy table
	 */
	public static final class Buddy implements BaseColumns {
		/**
		 * The content:// style URL for this table
		 */
		public static final ContentURI CONTENT_URI = ContentURI
				.create("content://org.yexing.android.sharepath.domain.SharePath/buddy");

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "modified DESC";

		/**
		 * The title of the note
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String TITLE = "title";

		/**
		 * The note itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String NOTE = "note";

		/**
		 * The timestamp for when the note was created
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String CREATED_DATE = "created";

		/**
		 * The timestamp for when the note was last modified
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String MODIFIED_DATE = "modified";
	}
}
