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

import java.util.HashMap;

//import android.content.ContentURIParser;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Convenience definitions for SharePathProvider
 */
public final class Domain {
	/**
	 * Message table
	 */
	public static final class Message implements BaseColumns {

		public static final int TYPE_INDEX = 1;
//		public static final int FROM_PERSON_INDEX = TYPE_INDEX + 1;
//		public static final int TO_PERSON_INDEX = FROM_PERSON_INDEX + 1;
		public static final int FROM_INDEX = TYPE_INDEX + 1;
		public static final int TO_INDEX = FROM_INDEX + 1;
		public static final int START_INDEX = TO_INDEX + 1;
		public static final int END_INDEX = START_INDEX + 1;
		public static final int DATE_INDEX = END_INDEX + 1;
		public static final int SENT_INDEX = DATE_INDEX + 1;
		public static final int READ_INDEX = SENT_INDEX + 1;
		public static final int LEVEL_INDEX = READ_INDEX + 1;
		public static final int CENTER_INDEX = LEVEL_INDEX + 1;
		public static final int PATH_INDEX = CENTER_INDEX + 1;

		public static final String[] PROJECTION = new String[] {
				Domain.Message._ID, Domain.Message.TYPE,
//				SharePath.Message.FROM_PERSON, SharePath.Message.TO_PERSON,
				Domain.Message.FROM, Domain.Message.TO,
				Domain.Message.START, Domain.Message.END,
				Domain.Message.DATE, Domain.Message.SENT,
				Domain.Message.READ, Domain.Message.LEVEL,
				Domain.Message.CENTER, Domain.Message.PATH };

		public static HashMap<String, String> MESSAGE_LIST_PROJECTION_MAP;
		
		static {
			MESSAGE_LIST_PROJECTION_MAP = new HashMap<String, String>();
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message._ID, Domain.Message._ID);
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message.TYPE, Domain.Message.TYPE);
//			MESSAGE_LIST_PROJECTION_MAP.put(SharePath.Message.FROM_PERSON, SharePath.Message.FROM_PERSON);
//			MESSAGE_LIST_PROJECTION_MAP.put(SharePath.Message.TO_PERSON, SharePath.Message.TO_PERSON);
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message.FROM, Domain.Message.FROM);
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message.TO, Domain.Message.TO);
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message.START, Domain.Message.START);
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message.END, Domain.Message.END);
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message.DATE, Domain.Message.DATE);
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message.SENT, Domain.Message.SENT);
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message.READ, Domain.Message.READ);
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message.LEVEL, Domain.Message.LEVEL);
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message.CENTER, Domain.Message.CENTER);
			MESSAGE_LIST_PROJECTION_MAP.put(Domain.Message.PATH, Domain.Message.PATH);
		}
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri
				.parse("content://org.yexing.android.sharepath.domain.SharePath/message");

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "_read ASC,_id DESC";

		/**
		 * The type of the message, 0:request 1:reply 2:record
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String TYPE = "_type";

//		/**
//		 * send from
//		 * <P>
//		 * Type: TEXT
//		 * </P>
//		 */
//		public static final String FROM_PERSON = "_from_person";
//
//		/**
//		 * send to
//		 * <P>
//		 * Type: TEXT
//		 * </P>
//		 */
//		public static final String TO_PERSON = "_to_person";
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
		 * start place
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String START = "_start";

		/**
		 * end place
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String END = "_end";

		/**
		 * The timestamp for when the message was sent
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String DATE = "_date";

		/**
		 * The sent flag 0:no 1:yes
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String SENT = "_sent";

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

		public static final int EMAIL_INDEX = 1;
		public static final int NAME_INDEX = EMAIL_INDEX + 1;
		public static final int POINT_INDEX = NAME_INDEX + 1;

		public static final String[] PROJECTION = new String[] {
				Domain.Buddy._ID, Domain.Buddy.EMAIL,
				Domain.Buddy.NAME, Domain.Buddy.POINT };

		public static HashMap<String, String> BUDDY_LIST_PROJECTION_MAP;
		
		static {
			BUDDY_LIST_PROJECTION_MAP = new HashMap<String, String>();
			BUDDY_LIST_PROJECTION_MAP.put(Domain.Buddy._ID, Domain.Buddy._ID);
			BUDDY_LIST_PROJECTION_MAP.put(Domain.Buddy.EMAIL, Domain.Buddy.EMAIL);
			BUDDY_LIST_PROJECTION_MAP.put(Domain.Buddy.NAME, Domain.Buddy.NAME);
			BUDDY_LIST_PROJECTION_MAP.put(Domain.Buddy.POINT, Domain.Buddy.POINT);
		}
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri
				.parse("content://org.yexing.android.sharepath.domain.SharePath/buddy");

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "_name ASC";

		/**
		 * buddy's email
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String EMAIL = "_email";

		/**
		 * buddy's name
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String NAME = "_name";

		/**
		 * buddy's point:one acceptable answer will win one point
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String POINT = "_point";

	}
}
