#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test032 {
		struct __A;
		struct __A_VT;


		typedef __A* A;

		struct __A {
			__A_VT* __vptr;

			__A();

			static Class __class();

			static __A_VT __vtable;

			static int32_t methodMInt(A, int32_t);
			static void methodMA(A, A);
			static void methodMDouble(A, double);
			static void methodMObject(A, Object);
			static void methodMObjectObject(A, Object, Object);
			static void methodMAObject(A, A, Object);
		};

		struct __A_VT {
			Class __isa;

			int32_t (*hashCode)(A);
			bool (*equals)(A, Object);
			Class (*getClass)(A);
			String (*toString)(A);
			int32_t (*methodMInt)(A, int32_t);
			void (*methodMA)(A, A);
			void (*methodMDouble)(A, double);
			void (*methodMObject)(A, Object);
			void (*methodMObjectObject)(A, Object, Object);
			void (*methodMAObject)(A, A, Object);

			__A_VT()
			: __isa(__A::__class()),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			toString((String(*)(A))&__Object::toString),
			methodMInt(&__A::methodMInt),
			methodMA(&__A::methodMA),
			methodMDouble(&__A::methodMDouble),
			methodMObject(&__A::methodMObject),
			methodMObjectObject(&__A::methodMObjectObject),
			methodMAObject(&__A::methodMAObject)
			{}
		};

	}
}
