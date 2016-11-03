#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test006 {
		struct __A;
		struct __A_VT;


		typedef __A* A;

		struct __A {
			__A_VT* __vptr;
			String fld;

			__A();

			static Class __class();

			static void setFld(A, String);
			static void almostSetFld(A, String);
			static String getFld(A);
		};

		struct __A_VT {
			Class __isa;

			void (*almostSetFld)(A, String);
			bool (*equals)(A, Object);
			Class (*getClass)(A);
			String (*getFld)(A);
			int32_t (*hashCode)(A);
			void (*setFld)(A, String);
			String (*toString)(A);

			__A_VT()
			: __isa(__A::__class()),
			almostSetFld(&__A::almostSetFld),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			getFld(&__A::getFld),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			setFld(&__A::setFld),
			toString((String(*)(A))&__Object::toString)
			{}
		};

	}
}
