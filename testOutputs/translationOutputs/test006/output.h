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

			static __A_VT __vtable;

			static void methodSetFld(A, String);
			static void methodAlmostSetFld(A, String);
			static String methodGetFld(A);
		};

		struct __A_VT {
			Class __isa;

			int32_t (*hashCode)(A);
			bool (*equals)(A, Object);
			Class (*getClass)(A);
			String (*toString)(A);
			void (*methodSetFld)(A, String);
			void (*methodAlmostSetFld)(A, String);
			String (*methodGetFld)(A);

			__A_VT()
			: __isa(__A::__class()),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			toString((String(*)(A))&__Object::toString),
			methodSetFld(&__A::methodSetFld),
			methodAlmostSetFld(&__A::methodAlmostSetFld),
			methodGetFld(&__A::methodGetFld)
			{}
		};

	}
}
