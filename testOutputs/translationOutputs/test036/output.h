#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test036 {
		struct __A;
		struct __A_VT;


		typedef __A* A;

		struct __A {
			__A_VT* __vptr;

			__A();

			static Class __class();

			static __A_VT __vtable;

			static void methodMObjectObject(A, Object, Object);
			static void methodMAObject(A, A, Object);
			static void methodMObjectA(A, Object, A);
		};

		struct __A_VT {
			Class __isa;

			int32_t (*hashCode)(A);
			bool (*equals)(A, Object);
			Class (*getClass)(A);
			String (*toString)(A);
			void (*methodMObjectObject)(A, Object, Object);
			void (*methodMAObject)(A, A, Object);
			void (*methodMObjectA)(A, Object, A);

			__A_VT()
			: __isa(__A::__class()),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			toString((String(*)(A))&__Object::toString),
			methodMObjectObject(&__A::methodMObjectObject),
			methodMAObject(&__A::methodMAObject),
			methodMObjectA(&__A::methodMObjectA)
			{}
		};

	}
}
