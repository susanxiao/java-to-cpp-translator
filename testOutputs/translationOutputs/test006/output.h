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
			String _fld;

			__A();

			static Class __class();

			static __A_VT __vtable;

			static void setFld(A, String);
			static void almostSetFld(A, String);
			static String getFld(A);
		};

		struct __A_VT {
			Class __isa;

			int32_t (*hashCode)(A);
			bool (*equals)(A, Object);
			Class (*getClass)(A);
			String (*toString)(A);
			void (*setFld)(A, String);
			void (*almostSetFld)(A, String);
			String (*getFld)(A);

			__A_VT()
			: __isa(__A::__class()),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			toString((String(*)(A))&__Object::toString),
			setFld(&__A::setFld),
			almostSetFld(&__A::almostSetFld),
			getFld(&__A::getFld)
			{}
		};

	}
}
